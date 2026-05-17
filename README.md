# cv-easy-maded

Backend-only Spring Boot 4 / Java 21 MVP for storing personal CV experience-history entries.

## LangGraph Workflow

This repository includes a sample LangGraph workflow under `tools/langgraph_workflow/`.

The workflow uses this sequence:

1. PM reads a Markdown brief and creates the feature description plus small TODO tasks.
2. TL reviews technical requirements, dependencies, risks, and feasibility.
3. Human approval is required before QA runs.
4. QA creates TDD scenarios, BDD scenarios, and coverage expectations.
5. Human approval is required before Dev runs.
6. Dev works only from the approved context and must not assume missing requirements.

## Runnable Commands

Create and activate a virtual environment:

```bash
python3 -m venv .venv
source .venv/bin/activate
```

Install the workflow dependency:

```bash
pip install -r tools/langgraph_workflow/requirements.txt
```

Run the workflow with the sample Markdown brief:

```bash
python3 tools/langgraph_workflow/workflow.py --input-file tools/langgraph_workflow/sample-brief.md
```

Run the backend tests:

```bash
./mvnw test
```

## Notes

- The workflow currently enforces the stage order and approval gates.
- Agent outputs are still pasted manually when prompted by the LangGraph runner.
- Project constraints and validation rules are defined in `CLAUDE.md`.
