# Sprint 01 QA: TDD Scenarios

## Purpose

Define the failing-first test scenarios for Sprint 01 so implementation can proceed in a TDD loop while respecting `CLAUDE.md` constraints.

## Test Strategy

- Prefer fast unit and lightweight context tests.
- Use `./mvnw test` as the validation command.
- Do not require a live MongoDB instance in the default test suite.
- Keep MongoDB integration tests out of Sprint 01 unless an explicit test-only strategy is added later.
- Treat `CLAUDE.md` and `specs/sprints2/01/tasks.md` as the source of truth when they conflict with feature files.

## Recommended Test Classes

- `src/test/java/com/vncrodrigues13/cv_easy_maded/ConfigurationPropertiesTest.java`
- `src/test/java/com/vncrodrigues13/cv_easy_maded/experience/ExperienceTypeTest.java`
- `src/test/java/com/vncrodrigues13/cv_easy_maded/experience/ExperienceEntryTest.java`
- Keep or adapt `CvEasyMadedApplicationTests` only if it can run without attempting a real MongoDB connection.

## Task-by-Task TDD Scenarios

### 1. Add Spring Data MongoDB support to `pom.xml` while keeping existing Spring Web MVC and test dependencies

TDD scenarios:

- Verify the Maven model contains a Spring Data MongoDB starter dependency.
- Verify `spring-boot-starter-webmvc` remains present.
- Verify `spring-boot-starter-webmvc-test` remains present with test scope.
- Verify no unrelated starters are added for AI, authentication, Docker, deployment, or CI behavior.

Suggested test approach:

- Use a simple XML parser test against `pom.xml`.
- Assert dependency artifact IDs rather than relying on line-by-line text matching.

### 2. Configure MongoDB in `application.properties`, keeping `spring.application.name=cv-easy-maded` and requiring `MONGODB_URI` as the primary connection source

TDD scenarios:

- Verify `spring.application.name` resolves to `cv-easy-maded`.
- Verify `spring.mongodb.uri` is configured from the `MONGODB_URI` placeholder.
- Verify the placeholder does not silently use an implicit local URI fallback.

Suggested test approach:

- Use Spring's property loading utilities or `ApplicationContextRunner`.
- Assert the raw property value contains `MONGODB_URI`.
- Assert the raw property value does not contain `mongodb://localhost`, `127.0.0.1`, or another fallback URI.

### 3. Do not provide an implicit local fallback MongoDB URI in application configuration

TDD scenarios:

- Verify `application.properties` does not define `${MONGODB_URI:mongodb://...}`.
- Verify default tests can set a test override if a context load requires Mongo properties.
- Verify no test depends on a developer's local MongoDB process.

Suggested test approach:

- Add a file-content guard test for forbidden fallback patterns.
- If `@SpringBootTest` needs a value, set a test-only property such as `spring.mongodb.uri=mongodb://example.invalid/test` and ensure no database operation is triggered.

### 4. Add database-name configuration only if the project needs it separately from the URI

TDD scenarios:

- Verify no separate `spring.mongodb.database` property is added by default.
- If a separate database property is introduced, verify there is a documented reason and it is environment-driven.
- Verify the application can rely on the database name contained in `MONGODB_URI`.

Suggested test approach:

- Add a configuration guard test asserting `spring.mongodb.database` is absent unless implementation explicitly justifies it.

### 5. Ensure `./mvnw test` can run without a live MongoDB instance by avoiding eager repository/database usage in default test paths

TDD scenarios:

- Verify the application context test starts without creating repository calls.
- Verify no test requires an open socket to MongoDB.
- Verify MongoDB client/repository beans are not used eagerly by startup code.

Suggested test approach:

- Keep startup tests limited to context creation.
- Avoid repository integration tests in Sprint 01.
- If a Mongo URI is required for context creation, use a non-routable or invalid host and assert tests still pass because no connection is attempted.

### 6. Create `ExperienceType.java` under the `experience` package with supported MVP values

TDD scenarios:

- Verify `ExperienceType` exists in `com.vncrodrigues13.cv_easy_maded.experience`.
- Verify enum values are exactly `PROJECT`, `TOOL`, `PROGRAMMING_LANGUAGE`, `CHALLENGE`, `BOOK`, `ARTICLE`, `PAPER`, and `OTHER`.
- Verify no extra enum values are introduced.

Suggested test approach:

- Unit test `ExperienceType.values()` and compare to the expected ordered list.

### 7. Create `ExperienceEntry.java` as a Spring Data MongoDB document under the `experience` package

TDD scenarios:

- Verify `ExperienceEntry` exists in `com.vncrodrigues13.cv_easy_maded.experience`.
- Verify it is annotated as a MongoDB document.
- Verify its `id` field is annotated as the Spring Data document ID.
- Verify it has no controller/API-specific annotations.

Suggested test approach:

- Unit test class and field annotations through reflection.
- Prefer annotation assertions over requiring a specific constructor style.

### 8. Include `id`, internal `userId`, `type`, `title`, `summary`, `tags`, `technologies`, `details`, `occurredAt`, `createdAt`, and `updatedAt` fields in `ExperienceEntry`

TDD scenarios:

- Verify all required fields exist with expected Java types.
- Verify `type` uses `ExperienceType`.
- Verify `id`, `userId`, `title`, and `summary` are string-like fields.
- Verify `occurredAt`, `createdAt`, and `updatedAt` use time-aware Java types such as `Instant`, `LocalDate`, or `LocalDateTime` according to the implementation decision.
- Verify there are no exposed account or authentication fields.

Suggested test approach:

- Unit test field existence and broad type contracts through reflection.
- Keep tests flexible where the spec permits an implementation choice, such as exact timestamp type.

### 9. Represent `details` as flexible `Map<String, Object>` data for entry-specific facts and future enriched facts

TDD scenarios:

- Verify `details` is assignable to `Map<String, Object>`.
- Verify `details` can hold string, number, boolean, list, and nested map values.
- Verify AI-enriched future facts can be represented as regular map data without adding AI-specific fields.

Suggested test approach:

- Unit test constructing an entry with representative nested details.
- Assert values can be retrieved with their original structure.

### 10. Represent `tags` and `technologies` as list-like fields for future filtering

TDD scenarios:

- Verify `tags` is list-like, such as `List<String>` or another `Collection<String>`.
- Verify `technologies` is list-like, such as `List<String>` or another `Collection<String>`.
- Verify both fields can hold multiple values.
- Verify both fields can be empty without requiring `null`.

Suggested test approach:

- Unit test field generic types through reflection if using `List<String>`.
- Add construction tests for empty and multi-value lists.

### 11. Keep the single-user assumption internal to backend domain/service logic and do not expose user-management behavior

TDD scenarios:

- Verify `ExperienceEntry` has `userId` for internal ownership.
- Verify no user account, authentication, or authorization classes are introduced.
- Verify no REST DTO or endpoint exposes user-management behavior in Sprint 01.
- Verify `userId` can default to a single-user constant once service/domain logic is added in later tasks.

Suggested test approach:

- For Sprint 01, add a package/file guard test that no `auth`, `authentication`, `authorization`, or `user` management packages are introduced.
- Defer service-level default-user behavior tests until a service exists.

### 12. Verify no AI, questionnaire, authentication, Docker, deployment, or CI dependencies/configuration are introduced

TDD scenarios:

- Verify `pom.xml` has no AI provider dependencies.
- Verify no `ai` or `questionnaire` endpoint packages are created.
- Verify no Spring Security dependency is added.
- Verify no Dockerfile, compose file, deployment manifest, or CI workflow is added.

Suggested test approach:

- Add repository guard tests that inspect dependency artifact IDs and selected filesystem paths.
- Keep these tests narrow so they enforce MVP boundaries without blocking normal Spring Boot files.

## BDD Acceptance Scenarios

### MongoDB configuration requires explicit URI

Given the backend application configuration is loaded
When the MongoDB URI is inspected
Then it is sourced from `MONGODB_URI`
And it does not define an implicit local fallback URI.

### Test suite does not require MongoDB

Given no live MongoDB instance is running
When `./mvnw test` is executed
Then the Sprint 01 test suite passes
And no default test performs a repository or database operation.

### Experience entry model supports MVP categories

Given the `ExperienceType` enum exists
When its values are inspected
Then it contains exactly the MVP categories `PROJECT`, `TOOL`, `PROGRAMMING_LANGUAGE`, `CHALLENGE`, `BOOK`, `ARTICLE`, `PAPER`, and `OTHER`.

### Experience entry model supports flexible facts

Given an `ExperienceEntry` is created
When details include nested and mixed-type values
Then the entry can retain those values in `Map<String, Object>` form
And no AI-specific schema field is required.

### MVP scope remains backend-only

Given Sprint 01 implementation is complete
When dependencies and repository files are inspected
Then no AI, questionnaire, authentication, Docker, deployment, or CI scope has been introduced.

## Coverage Expectations

- Line coverage should include all new domain model methods that contain behavior, including constructors, factories, or defaulting helpers.
- Branch coverage should include any timestamp/defaulting logic if the implementation adds it.
- Configuration guard tests should cover both required properties and forbidden fallback/scope patterns.
- Do not count reflection-only tests as sufficient for future service or REST behavior; later sprints need unit and MVC tests for repository, service, and controller flows.

## Remaining Risks

- Reflection tests can become brittle if they over-specify implementation style. Keep them focused on contract-level requirements from the sprint.
- `@SpringBootTest` can accidentally become a MongoDB integration test if startup code performs database operations later.
- Database-name behavior remains intentionally minimal; add tests only if the implementation introduces a separate property.
