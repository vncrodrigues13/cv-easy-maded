# Feature: Expose experience entry REST API

## Goal

Expose REST endpoints for managing CV experience-history entries.

## Context

The MVP is backend-only. Users will directly submit completed experience data through HTTP requests. AI-assisted question generation is intentionally left for later.

## Scope

Create API files under `src/main/java/com/vncrodrigues13/cv_easy_maded/experience/` or an `experience/dto/` subpackage:

- `ExperienceEntryController.java`
- `CreateExperienceEntryRequest`
- `UpdateExperienceEntryRequest`
- `ExperienceEntryResponse`

Base path:

`/api/experience-entries`

Endpoints:

- `POST /api/experience-entries`
- `POST /api/experience-entries/bulk`
- `GET /api/experience-entries`
- `GET /api/experience-entries/{id}`
- `PUT /api/experience-entries/{id}`
- `DELETE /api/experience-entries/{id}`

List filters:

- `type`
- `tag`
- `technology`

## API behavior

- Create should return the created entry response.
- Bulk create should return all created entry responses.
- List should return entries for the single default user.
- Get should return one entry or a not-found response.
- Put should replace editable fields for an existing entry.
- Delete should remove the entry and return a successful empty response.
- API responses should not require clients to provide or manage `userId`.

## Out of scope

- Authentication.
- Authorization.
- Pagination.
- PATCH endpoints.
- AI/questionnaire endpoints.

## TODO

- [ ] Create request DTO for single create.
- [ ] Create request DTO for full update.
- [ ] Create response DTO.
- [ ] Create controller with base path `/api/experience-entries`.
- [ ] Implement single create endpoint.
- [ ] Implement bulk create endpoint.
- [ ] Implement list endpoint with optional filters.
- [ ] Implement get-by-ID endpoint.
- [ ] Implement full update endpoint.
- [ ] Implement delete endpoint.
- [ ] Map service errors to appropriate HTTP responses.

## Definition of done

- All planned REST endpoints exist.
- Request and response bodies use DTOs rather than exposing persistence concerns directly.
- Clients can create, bulk create, list/filter, read, update, and delete entries.
- The API remains single-user for the MVP and does not expose user-management concerns.
