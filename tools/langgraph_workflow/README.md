# LangGraph Workflow Sample

This folder contains a minimal LangGraph implementation of the workflow you described.

The workflow input is a Markdown brief file. That file is the AI-authored planning input that the PM stage reads first.

1. PM receives a Markdown brief and writes the feature description plus small TODO tasks.
2. TL reviews the plan for technical requirements, dependencies, risks, and feasibility.
3. Human approval is required before QA runs.
4. QA creates TDD scenarios, BDD scenarios, and coverage expectations.
5. Human approval is required before Dev runs.
6. Dev works only from the approved context and must not assume missing requirements.

## What this implementation does

- Uses LangGraph to enforce the stage order.
- Uses LangGraph interrupts for the two approval gates.
- Uses LangGraph interrupts to collect each agent's output.
- Keeps the workflow isolated from the Spring Boot application code.

## What this implementation does not do yet

- It does not directly call OpenAI, Claude, or any other provider.
- It does not persist checkpoints across separate processes.
- It does not update specs or source files automatically.

The current version is a hard orchestration skeleton: the graph controls the order and stop points, and you paste each agent's output into the workflow when prompted.

## Install

```bash
python3 -m venv .venv
source .venv/bin/activate
pip install -r tools/langgraph_workflow/requirements.txt
```

## Run

Create an input brief such as `tools/langgraph_workflow/sample-brief.md`:

```md
# Feature Brief

Add bulk experience entry import validation.

## Goals

- Reject malformed bulk items clearly.
- Keep MVP validation minimal.
- Preserve flexible details payloads.
```

Run the workflow with that file:

```bash
python3 tools/langgraph_workflow/workflow.py --input-file tools/langgraph_workflow/sample-brief.md
```

At each step, the workflow prints:

- which agent should act
- the context for that agent
- the required output shape

Paste the corresponding agent output and finish with a line containing only `END`.

For approval stages, answer `y` or `yes` to continue. Any other answer stops the workflow before the gated stage.

## Next extension

If you want, this can be extended to call real LLM-backed agents directly instead of asking you to paste their outputs manually.
