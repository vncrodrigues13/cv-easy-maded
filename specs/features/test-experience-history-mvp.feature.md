# Feature: Test experience history MVP

## Goal

Add automated coverage for the MongoDB-backed experience-history MVP.

## Context

The original project only has a generated application context-load test. The MVP adds domain, service, repository, and controller behavior that should be covered before implementation is considered complete.

## Scope

Create or update tests under `src/test/java/com/vncrodrigues13/cv_easy_maded/`:

- `experience/ExperienceEntryServiceTest.java`
- `experience/ExperienceEntryControllerTest.java`
- `CvEasyMadedApplicationTests.java`

Service test coverage:

- Default user assignment.
- `createdAt` and `updatedAt` handling on create.
- `updatedAt` handling on update.
- Required-field validation.
- Repository interaction for create/list/get/update/delete.
- Bulk create all-or-nothing validation behavior.

Controller test coverage:

- Create request/response behavior.
- Bulk create request/response behavior.
- List endpoint and optional filters.
- Get-by-ID success and not-found behavior.
- Update request/response behavior.
- Delete behavior.
- Validation error responses.

Application test coverage:

- Spring context still loads with MongoDB configuration present.

## Test strategy

Prefer fast tests that do not require a manually running MongoDB instance:

- Service tests may mock the repository.
- Controller tests may use Spring MVC test support and mock the service.
- Context-load tests should avoid requiring a live MongoDB connection unless Testcontainers or another explicit integration-test setup is added.

## Out of scope

- End-to-end tests against a real MongoDB instance.
- Performance tests.
- AI-related tests.
- Browser/UI tests.

## TODO

- [ ] Add service tests for create behavior and default fields.
- [ ] Add service tests for list/get/update/delete behavior.
- [ ] Add service tests for validation failures.
- [ ] Add service tests for bulk all-or-nothing behavior.
- [ ] Add controller tests for every REST endpoint.
- [ ] Add controller tests for filters.
- [ ] Add controller tests for not-found and validation error cases.
- [ ] Keep or update the generated context-load test.
- [ ] Run `./mvnw test` successfully.

## Definition of done

- Service behavior is covered without depending on a live MongoDB instance.
- REST API behavior is covered with Spring MVC tests.
- The application context test passes.
- `./mvnw test` passes locally.
