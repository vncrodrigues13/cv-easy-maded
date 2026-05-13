# Sprint 04: REST API

Source features:

- `../../features/experience-rest-api.feature.md`
- `../../features/defer-ai-entry-enrichment.feature.md`

## Tasks

- [ ] Create request DTOs for single create, bulk create item reuse, and full update under the `experience` package or an `experience/dto` subpackage.
- [ ] Create `ExperienceEntryResponse` so API responses do not expose persistence concerns or require clients to manage `userId`.
- [ ] Create `ExperienceEntryController.java` with base path `/api/experience-entries`.
- [ ] Implement `POST /api/experience-entries`, `POST /api/experience-entries/bulk`, and `GET /api/experience-entries` with optional `type`, `tag`, and `technology` filters.
- [ ] Implement `GET /api/experience-entries/{id}`, `PUT /api/experience-entries/{id}`, `DELETE /api/experience-entries/{id}`, and HTTP mapping for validation and not-found errors.

## Definition of Done

- All planned REST endpoints exist.
- Requests and responses use DTOs rather than exposing MongoDB documents directly.
- Clients can create, bulk create, list/filter, read, update, and delete completed entries.
- The API contains no AI/questionnaire endpoints and no authentication concerns.
