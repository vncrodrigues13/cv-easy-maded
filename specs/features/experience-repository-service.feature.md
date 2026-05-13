# Feature: Implement experience repository and service

## Goal

Add persistence and business logic for creating, listing, reading, updating, and deleting experience entries for the single-user MVP.

## Context

The backend needs a service layer that owns default single-user behavior, timestamps, validation orchestration, and repository access. The repository should support common listing and filtering cases.

## Scope

Create files under `src/main/java/com/vncrodrigues13/cv_easy_maded/experience/`:

- `ExperienceEntryRepository.java`
- `ExperienceEntryService.java`

Repository behavior:

- Save entries.
- Find entries by ID for the default user.
- List entries for the default user.
- Optionally filter by `type`, `tag`, and `technology`.
- Delete entries by ID for the default user.

Service behavior:

- Default every entry to the single MVP user.
- Set `createdAt` and `updatedAt` when creating entries.
- Update `updatedAt` when replacing an entry.
- Validate required fields before persistence.
- Support single create and bulk create.
- Support list, get, update, and delete operations.

## Validation rules

Minimum MVP validation:

- `type` is required.
- `title` is required and must not be blank.
- `tags`, `technologies`, and `details` should safely default to empty values when omitted.

## Bulk behavior

Bulk create should validate all requested entries before saving. If any entry is invalid, the request should fail without saving a partial set.

## Out of scope

- Multi-user data isolation beyond the default user constant.
- Partial update/PATCH behavior.
- Advanced search or indexing.
- AI-generated follow-up questions.

## TODO

- [ ] Create `ExperienceEntryRepository.java` extending the Spring Data MongoDB repository type.
- [ ] Add query methods for default-user listing and filtering.
- [ ] Create `ExperienceEntryService.java`.
- [ ] Implement single create.
- [ ] Implement bulk create with all-or-nothing validation.
- [ ] Implement list with optional filters.
- [ ] Implement get by ID scoped to the default user.
- [ ] Implement full update/replace behavior.
- [ ] Implement delete scoped to the default user.
- [ ] Add required-field validation.
- [ ] Add timestamp handling.

## Definition of done

- Service methods cover all MVP CRUD and bulk operations.
- Entries are always scoped to the default single user.
- Timestamps are handled consistently.
- Invalid entries are rejected before persistence.
- Repository methods support the planned controller filters.
