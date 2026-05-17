# Sprint 02: Experience Entry Model

Source features:

- `../../features/model-experience-entries.feature.md`
- `../../features/defer-ai-entry-enrichment.feature.md`

## Tasks

- [ ] Create the `experience` package under `src/main/java/com/vncrodrigues13/cv_easy_maded/`.
- [ ] Add `ExperienceType.java` with `PROJECT`, `TOOL`, `PROGRAMMING_LANGUAGE`, `CHALLENGE`, `BOOK`, `ARTICLE`, `PAPER`, and `OTHER`.
- [ ] Add `ExperienceEntry.java` as a Spring Data MongoDB document with `id`, internal `userId`, `type`, `title`, `summary`, `tags`, `technologies`, `details`, `occurredAt`, `createdAt`, and `updatedAt`.
- [ ] Model `tags` and `technologies` as list-like fields and `details` as `Map<String, Object>` for flexible completed-entry facts.
- [ ] Centralize the single-user default in the domain/service boundary and keep AI/questionnaire concepts out of the model.

## Definition of Done

- Experience entries support all MVP categories.
- The document shape supports filtering by type, tag, and technology.
- Flexible details can store entry-specific facts without schema changes.
- No client-facing user-management or AI fields are added.

## Open Questions

- Should Sprint 02 define an actual default user constant now, or should it only ensure `userId` exists internally and leave default assignment to Sprint 03 service work?
