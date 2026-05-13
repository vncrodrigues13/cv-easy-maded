# Feature: Defer AI-assisted entry enrichment

## Goal

Keep the MVP design ready for future AI-assisted questionnaire/enrichment flows without implementing AI integration now.

## Context

The long-term product may ask follow-up questions and enrich draft experience entries before saving them. The current MVP should only store completed entry data submitted directly through the REST API.

## Scope

- Avoid OpenAI or other AI provider integration in the MVP.
- Avoid creating questionnaire endpoints in the MVP.
- Keep the experience-entry model flexible enough for future enriched data through `details`.
- Keep package structure compatible with adding a future `questionnaire` or `ai` package.

Future AI flow, not implemented now:

1. Accept a draft entry.
2. Generate follow-up questions.
3. Collect answers.
4. Enrich and save the final `ExperienceEntry`.

## Out of scope

- AI dependencies.
- Prompt design.
- LLM API calls.
- Questionnaire storage.
- Async workflows.

## TODO

- [ ] Do not add AI dependencies to `pom.xml`.
- [ ] Do not create AI or questionnaire endpoints in the MVP.
- [ ] Ensure the `details` field can store future enriched facts.
- [ ] Keep current API focused on direct CRUD for completed entries.

## Definition of done

- The MVP contains no AI provider integration.
- The REST API only accepts completed experience entry data.
- The data model can still store future enriched information without schema changes.
