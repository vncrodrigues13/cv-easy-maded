# Sprint 03: Repository And Service

Source features:

- `../../features/experience-repository-service.feature.md`

## Tasks

- [ ] Create `ExperienceEntryRepository.java` extending the appropriate Spring Data MongoDB repository type.
- [ ] Add repository methods for default-user read, list, optional type/tag/technology filtering, and delete-by-id behavior.
- [ ] Create `ExperienceEntryService.java` to own default single-user behavior, validation orchestration, timestamp handling, and repository access.
- [ ] Implement create and bulk-create operations, including all-or-nothing validation before persistence.
- [ ] Implement list, get-by-id, full update/replace, and delete operations scoped to the default single user.

## Definition of Done

- Service methods cover all MVP CRUD and bulk operations.
- `type` and non-blank `title` are required before persistence.
- Missing `tags`, `technologies`, and `details` safely default to empty values.
- `createdAt` and `updatedAt` are handled consistently.
