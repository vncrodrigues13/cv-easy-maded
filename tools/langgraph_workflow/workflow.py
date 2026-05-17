from __future__ import annotations

import argparse
import json
from operator import add
from pathlib import Path
from typing import Annotated, Any, Literal, TypedDict

from langgraph.checkpoint.memory import MemorySaver
from langgraph.graph import END, START, StateGraph
from langgraph.types import Command, interrupt


class WorkflowState(TypedDict, total=False):
    brief_path: str
    brief_markdown: str
    planner_output: str
    tech_lead_output: str
    qa_output: str
    dev_output: str
    qa_approved: bool
    dev_approved: bool
    status: str
    history: Annotated[list[str], add]


def planner_stage(state: WorkflowState) -> WorkflowState:
    response = interrupt(
        {
            "type": "agent_task",
            "agent": "pm",
            "title": "Create feature spec and split it into small TODO tasks",
            "context": {
                "brief_path": state["brief_path"],
                "brief_markdown": state["brief_markdown"],
                "required_project_context": "CLAUDE.md",
            },
            "instructions": [
                "Use the Markdown brief as the source feature input.",
                "Describe the feature clearly.",
                "Split the work into small implementation tasks.",
                "Write TODO items using '- [ ] task text'.",
                "Define clear Definition of Done criteria.",
            ],
            "output_contract": [
                "feature summary",
                "scope",
                "task breakdown",
                "TODO checklists",
                "definition of done",
            ],
        }
    )
    return {
        "planner_output": extract_text(response),
        "status": "planned",
        "history": ["planner completed"],
    }


def tech_lead_stage(state: WorkflowState) -> WorkflowState:
    response = interrupt(
        {
            "type": "agent_task",
            "agent": "tl",
            "title": "Review the planned work for technical requirements and risks",
            "context": {
                "brief_path": state["brief_path"],
                "brief_markdown": state["brief_markdown"],
                "planner_output": state["planner_output"],
                "required_project_context": "CLAUDE.md",
            },
            "instructions": [
                "Review the plan for feasibility.",
                "Identify dependency changes or new technical requirements.",
                "Flag risks, ambiguity, security concerns, and TODO ordering issues.",
                "Do not make final technical decisions that require human approval.",
            ],
            "output_contract": [
                "technical proposal",
                "dependency concerns",
                "security considerations",
                "feasibility risks",
                "todo ordering recommendations",
                "human decisions needed",
            ],
        }
    )
    return {
        "tech_lead_output": extract_text(response),
        "status": "tech-reviewed",
        "history": ["tech lead review completed"],
    }


def qa_approval_stage(state: WorkflowState) -> WorkflowState:
    response = interrupt(
        {
            "type": "approval",
            "stage": "qa",
            "title": "Approve QA planning",
            "question": "Do you approve the planner and tech lead outputs so QA can proceed?",
            "context": {
                "planner_output": state["planner_output"],
                "tech_lead_output": state["tech_lead_output"],
            },
        }
    )
    approved = extract_approval(response)
    return {
        "qa_approved": approved,
        "status": "qa-approved" if approved else "stopped-before-qa",
        "history": ["qa approved" if approved else "qa approval denied"],
    }


def qa_stage(state: WorkflowState) -> WorkflowState:
    response = interrupt(
        {
            "type": "agent_task",
            "agent": "qa",
            "title": "Create test scenarios and coverage expectations",
            "context": {
                "brief_path": state["brief_path"],
                "brief_markdown": state["brief_markdown"],
                "planner_output": state["planner_output"],
                "tech_lead_output": state["tech_lead_output"],
                "required_project_context": "CLAUDE.md",
            },
            "instructions": [
                "Create TDD unit and integration scenarios.",
                "Create BDD scenarios in Given/When/Then form.",
                "Define line and branch coverage expectations.",
                "Call out gaps, risks, and recommended next tests.",
            ],
            "output_contract": [
                "tdd scenarios",
                "bdd scenarios",
                "coverage expectations",
                "validation report",
            ],
        }
    )
    return {
        "qa_output": extract_text(response),
        "status": "qa-planned",
        "history": ["qa planning completed"],
    }


def dev_approval_stage(state: WorkflowState) -> WorkflowState:
    response = interrupt(
        {
            "type": "approval",
            "stage": "dev",
            "title": "Approve implementation",
            "question": "Do you approve the planner, tech lead, and QA outputs so development can proceed?",
            "context": {
                "planner_output": state["planner_output"],
                "tech_lead_output": state["tech_lead_output"],
                "qa_output": state["qa_output"],
            },
        }
    )
    approved = extract_approval(response)
    return {
        "dev_approved": approved,
        "status": "dev-approved" if approved else "stopped-before-dev",
        "history": ["dev approved" if approved else "dev approval denied"],
    }


def dev_stage(state: WorkflowState) -> WorkflowState:
    response = interrupt(
        {
            "type": "agent_task",
            "agent": "dev",
            "title": "Implement the approved work without assuming missing requirements",
            "context": {
                "brief_path": state["brief_path"],
                "brief_markdown": state["brief_markdown"],
                "planner_output": state["planner_output"],
                "tech_lead_output": state["tech_lead_output"],
                "qa_output": state["qa_output"],
                "required_project_context": "CLAUDE.md",
            },
            "instructions": [
                "Implement only what is described in the approved planning artifacts.",
                "Do not assume missing behavior, API semantics, or validation rules.",
                "If anything is ambiguous, stop and ask the human before coding.",
                "Report what was implemented and what remains blocked by questions.",
            ],
            "output_contract": [
                "implementation summary",
                "files changed",
                "tests added or updated",
                "open questions or blockers",
            ],
        }
    )
    return {
        "dev_output": extract_text(response),
        "status": "implemented",
        "history": ["development completed"],
    }


def route_after_qa_approval(state: WorkflowState) -> Literal["qa_stage", END]:
    return "qa_stage" if state.get("qa_approved") else END


def route_after_dev_approval(state: WorkflowState) -> Literal["dev_stage", END]:
    return "dev_stage" if state.get("dev_approved") else END


def build_graph():
    graph = StateGraph(WorkflowState)
    graph.add_node("planner_stage", planner_stage)
    graph.add_node("tech_lead_stage", tech_lead_stage)
    graph.add_node("qa_approval_stage", qa_approval_stage)
    graph.add_node("qa_stage", qa_stage)
    graph.add_node("dev_approval_stage", dev_approval_stage)
    graph.add_node("dev_stage", dev_stage)

    graph.add_edge(START, "planner_stage")
    graph.add_edge("planner_stage", "tech_lead_stage")
    graph.add_edge("tech_lead_stage", "qa_approval_stage")
    graph.add_conditional_edges("qa_approval_stage", route_after_qa_approval, ["qa_stage", END])
    graph.add_edge("qa_stage", "dev_approval_stage")
    graph.add_conditional_edges("dev_approval_stage", route_after_dev_approval, ["dev_stage", END])
    graph.add_edge("dev_stage", END)

    return graph.compile(checkpointer=MemorySaver())


def extract_text(response: Any) -> str:
    if isinstance(response, str):
        return response.strip()
    if isinstance(response, dict):
        for key in ("content", "output", "text"):
            value = response.get(key)
            if isinstance(value, str) and value.strip():
                return value.strip()
        return json.dumps(response, indent=2)
    return str(response).strip()


def extract_approval(response: Any) -> bool:
    if isinstance(response, bool):
        return response
    if isinstance(response, str):
        return response.strip().lower() in {"y", "yes", "true", "approved", "approve"}
    if isinstance(response, dict):
        if "approved" in response:
            return bool(response["approved"])
        if "answer" in response:
            return extract_approval(response["answer"])
    return False


def interrupt_payload(result: dict[str, Any]) -> Any | None:
    interrupts = result.get("__interrupt__")
    if not interrupts:
        return None
    first = interrupts[0]
    return getattr(first, "value", first)


def prompt_for_response(payload: Any) -> Any:
    if not isinstance(payload, dict):
        print(payload)
        return input("Response: ").strip()

    if payload.get("type") == "approval":
        print_section(payload.get("title", "Approval"))
        print(payload.get("question", "Approve?"))
        answer = input("Approve? [y/N]: ").strip().lower()
        return {"approved": answer in {"y", "yes"}}

    print_section(payload.get("title", "Agent task"))
    print(f"Agent: @{payload.get('agent', 'unknown')}")
    print("Context:")
    print(json.dumps(payload.get("context", {}), indent=2))
    print("Instructions:")
    for item in payload.get("instructions", []):
        print(f"- {item}")
    print("Output contract:")
    for item in payload.get("output_contract", []):
        print(f"- {item}")
    print("Paste the agent output below. Finish with a line that contains only END.")
    return {"content": read_multiline()}


def read_multiline() -> str:
    lines: list[str] = []
    while True:
        line = input()
        if line == "END":
            break
        lines.append(line)
    return "\n".join(lines).strip()


def print_section(title: str) -> None:
    print()
    print(f"=== {title} ===")


def read_markdown_brief(input_file: str) -> tuple[str, str]:
    path = Path(input_file).expanduser().resolve()
    return str(path), path.read_text(encoding="utf-8")


def run_interactive(input_file: str, thread_id: str) -> WorkflowState:
    workflow = build_graph()
    config = {"configurable": {"thread_id": thread_id}}
    brief_path, brief_markdown = read_markdown_brief(input_file)
    result = workflow.invoke(
        {
            "brief_path": brief_path,
            "brief_markdown": brief_markdown,
            "status": "started",
            "history": [],
        },
        config=config,
    )

    while True:
        payload = interrupt_payload(result)
        if payload is None:
            return result
        response = prompt_for_response(payload)
        result = workflow.invoke(Command(resume=response), config=config)


def main() -> None:
    parser = argparse.ArgumentParser(description="Run the LangGraph PM -> TL -> QA -> Dev workflow.")
    parser.add_argument("--input-file", required=True, help="Markdown brief file to send through the workflow.")
    parser.add_argument(
        "--thread-id",
        default="sample-workflow",
        help="Checkpoint thread id used by the in-memory LangGraph checkpointer.",
    )
    args = parser.parse_args()

    final_state = run_interactive(args.input_file, args.thread_id)
    print_section("Final state")
    print(json.dumps(final_state, indent=2))


if __name__ == "__main__":
    main()
