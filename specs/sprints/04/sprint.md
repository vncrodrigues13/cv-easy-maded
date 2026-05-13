# Sprint 04: Test Coverage and MVP Boundary Checks

## Goal

Add automated coverage for MVP behavior and verify that non-MVP AI/questionnaire work remains deferred.

## Ordered tasks

1. Add service tests for create, defaults, and validation
   - Source feature: `../../features/test-experience-history-mvp.feature.md`
   - Cover default user assignment.
   - Cover createdAt and updatedAt on create.
   - Cover required-field validation failures.

2. Add service tests for list/get/update/delete and bulk behavior
   - Source feature: `../../features/test-experience-history-mvp.feature.md`
   - Cover repository interaction for list/get/update/delete.
   - Cover updatedAt on update.
   - Cover bulk all-or-nothing validation.

3. Add controller endpoint tests
   - Source feature: `../../features/test-experience-history-mvp.feature.md`
   - Cover create, bulk create, list/filter, get, update, and delete request/response behavior.
   - Cover not-found and validation error responses.

4. Keep application context test passing
   - Source feature: `../../features/test-experience-history-mvp.feature.md`
   - Keep or update `CvEasyMadedApplicationTests.java`.
   - Ensure context loading does not require a manually running MongoDB instance.

5. Verify deferred AI boundaries
   - Source feature: `../../features/defer-ai-entry-enrichment.feature.md`
   - Confirm no AI dependencies are added to `pom.xml`.
   - Confirm no AI or questionnaire endpoints are created.
   - Confirm the API remains direct CRUD for completed entries and `details` can hold future enriched facts.

## Definition of done

- Service behavior is covered without a live MongoDB dependency.
- REST behavior is covered with Spring MVC tests.
- The application context test passes.
- The MVP contains no AI provider integration, AI endpoints, or questionnaire endpoints.
- `./mvnw test` passes locally.
