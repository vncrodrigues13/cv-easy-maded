# Sprint 03: Service Completion and REST API

## Goal

Complete service operations and expose the MVP REST API for direct CRUD and bulk entry submission.

## Ordered tasks

1. Implement bulk create behavior
   - Source feature: `../../features/experience-repository-service.feature.md`
   - Validate all entries before saving.
   - Fail without partial persistence if any entry is invalid.

2. Implement update and delete service behavior
   - Source feature: `../../features/experience-repository-service.feature.md`
   - Implement full replacement update scoped to the default user.
   - Update updatedAt during replacement.
   - Implement delete scoped to the default user.

3. Create REST DTOs
   - Source feature: `../../features/experience-rest-api.feature.md`
   - Create single-create request DTO.
   - Create full-update request DTO.
   - Create response DTO.
   - Do not expose user-management concerns to clients.

4. Create controller and CRUD endpoints
   - Source feature: `../../features/experience-rest-api.feature.md`
   - Create `ExperienceEntryController.java` with base path `/api/experience-entries`.
   - Implement create, list/filter, get by ID, update, and delete endpoints.

5. Add bulk endpoint and HTTP error mapping
   - Source feature: `../../features/experience-rest-api.feature.md`
   - Implement `POST /api/experience-entries/bulk`.
   - Map not-found and validation service errors to appropriate HTTP responses.

## Definition of done

- Service supports all MVP CRUD and bulk operations.
- REST API exposes create, bulk create, list/filter, get, update, and delete.
- API clients do not provide or manage `userId`.
- No authentication, authorization, pagination, PATCH, questionnaire, or AI endpoints are added.
