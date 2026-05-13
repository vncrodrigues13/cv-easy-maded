# Sprint 05: MVP Test Coverage

Source features:

- `../../features/test-experience-history-mvp.feature.md`

## Tasks

- [ ] Add `experience/ExperienceEntryServiceTest.java` covering default user assignment, create timestamps, update timestamps, validation failures, and repository interactions.
- [ ] Add service tests for list, get, update, delete, and bulk-create all-or-nothing behavior.
- [ ] Add `experience/ExperienceEntryControllerTest.java` covering create, bulk create, list/filter, get success, get not found, update, delete, and validation responses.
- [ ] Keep or update `CvEasyMadedApplicationTests.java` so the Spring context loads with MongoDB configuration present but without requiring a live MongoDB instance.
- [ ] Run `./mvnw test` and address failures caused by the MVP implementation.

## Definition of Done

- Service behavior is covered with fast tests that do not require a live MongoDB instance.
- REST API behavior is covered with Spring MVC tests.
- The application context test passes.
- `./mvnw test` passes locally.
