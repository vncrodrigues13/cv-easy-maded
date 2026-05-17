# Sprint 02 QA: TDD Scenarios

## Purpose

Define failing-first test scenarios for Sprint 02 so the experience-entry model can be implemented in a TDD loop while respecting `CLAUDE.md` MVP constraints.

## Test Strategy

- Prefer fast unit tests and lightweight reflection tests.
- Use `./mvnw test` as the validation command.
- Do not require a live MongoDB instance.
- Keep repository, service, REST, authentication, and AI/questionnaire behavior out of this sprint.
- Treat `specs/sprints2/02/tasks.md` as the sprint source of truth.
- Resolve the open default-user question by testing only that `userId` exists as internal backend model data; default user assignment belongs to Sprint 03 service work.

## Recommended Test Classes

- `src/test/java/com/vncrodrigues13/cv_easy_maded/experience/ExperienceTypeTest.java`
- `src/test/java/com/vncrodrigues13/cv_easy_maded/experience/ExperienceEntryTest.java`
- `src/test/java/com/vncrodrigues13/cv_easy_maded/ConfigurationPropertiesTest.java` for dependency and package boundary guards if not already covered by Sprint 01.

## Task-by-Task TDD Scenarios

### 1. Create the `experience` package under `src/main/java/com/vncrodrigues13/cv_easy_maded/`

TDD scenarios:

- Verify `ExperienceType` exists in package `com.vncrodrigues13.cv_easy_maded.experience`.
- Verify `ExperienceEntry` exists in package `com.vncrodrigues13.cv_easy_maded.experience`.
- Verify Sprint 02 domain model classes are not created in controller, repository, service, AI, questionnaire, auth, or user-management packages.

Suggested test approach:

- Unit test `Class.getPackageName()` for model classes.
- Add or reuse a narrow filesystem guard test for forbidden package names.

### 2. Add `ExperienceType.java` with `PROJECT`, `TOOL`, `PROGRAMMING_LANGUAGE`, `CHALLENGE`, `BOOK`, `ARTICLE`, `PAPER`, and `OTHER`

TDD scenarios:

- Verify `ExperienceType.values()` contains exactly the supported MVP values.
- Verify no extra values are added.
- Verify the enum lives in the `experience` package.
- Verify out-of-scope values such as `AI_DRAFT`, `QUESTIONNAIRE`, `USER`, `CERTIFICATION`, or `COURSE` are absent unless a later approved feature changes the scope.

Suggested test approach:

- Unit test `ExperienceType.values()` against an ordered expected array.
- Keep the test strict because these enum values are part of the model contract.

### 3. Add `ExperienceEntry.java` as a Spring Data MongoDB document with all required fields

TDD scenarios:

- Verify `ExperienceEntry` is annotated with Spring Data MongoDB `@Document`.
- Verify the document has an `id` field annotated with Spring Data `@Id`.
- Verify a public or package-visible no-args constructor exists for Spring Data mapping.
- Verify all required fields exist:
  - `id`
  - `userId`
  - `type`
  - `title`
  - `summary`
  - `tags`
  - `technologies`
  - `details`
  - `occurredAt`
  - `createdAt`
  - `updatedAt`
- Verify field types match the model contract:
  - `id`, `userId`, `title`, and `summary` are `String`.
  - `type` is `ExperienceType`.
  - `tags` and `technologies` are list-like string collections.
  - `details` is `Map<String, Object>`.
  - timestamp/date fields use a Java date/time type, preferably `Instant`.

Suggested test approach:

- Use reflection for annotations, field names, and field types.
- Add accessor round-trip tests for all fields so implementation is usable beyond reflection.

### 4. Model `tags` and `technologies` as list-like fields and `details` as `Map<String, Object>` for flexible completed-entry facts

TDD scenarios:

- Verify `tags` can store multiple string values.
- Verify `technologies` can store multiple string values.
- Verify `tags` and `technologies` can be empty without being `null`.
- Verify omitted or explicitly null `tags` and `technologies` default to empty collections.
- Verify `details` can store mixed completed-entry facts:
  - string values
  - number values
  - boolean values
  - lists
  - nested maps
- Verify omitted or explicitly null `details` defaults to an empty map.
- Verify setters copy incoming collections and maps so later caller-side mutation does not mutate the entry.

Suggested test approach:

- Use regular unit tests that construct `ExperienceEntry`, set representative values, and assert getters return the expected structure.
- Add branch coverage for null and non-null setter paths.

### 5. Centralize the single-user default in the domain/service boundary and keep AI/questionnaire concepts out of the model

TDD scenarios:

- Verify `ExperienceEntry` includes `userId` as internal backend ownership data.
- Verify `ExperienceEntry` does not include client-facing user-management fields such as `username`, `email`, `owner`, `tenantId`, `password`, `roles`, or `permissions`.
- Verify `ExperienceEntry` does not include AI/questionnaire-specific fields such as `prompt`, `questions`, `answers`, `enrichment`, `provider`, `model`, or `llm`.
- Verify no `ai`, `questionnaire`, `auth`, `authentication`, or `authorization` packages are introduced by Sprint 02.
- Verify `pom.xml` does not add AI provider or Spring Security dependencies.
- Do not test default-user assignment in Sprint 02; that behavior belongs to Sprint 03 `ExperienceEntryService`.

Suggested test approach:

- Use reflection to inspect `ExperienceEntry` field names.
- Use a focused filesystem/dependency guard test for forbidden packages and dependencies.

## BDD Acceptance Scenarios

### Experience package contains the model

Given Sprint 02 model work is implemented
When the Java package structure is inspected
Then `ExperienceType` and `ExperienceEntry` exist under `com.vncrodrigues13.cv_easy_maded.experience`
And no Sprint 02 model class is placed in service, controller, AI, questionnaire, auth, or user-management packages.

### Supported experience categories are complete

Given the `ExperienceType` enum exists
When its values are inspected
Then it contains exactly `PROJECT`, `TOOL`, `PROGRAMMING_LANGUAGE`, `CHALLENGE`, `BOOK`, `ARTICLE`, `PAPER`, and `OTHER`
And it contains no speculative categories.

### Experience entry is a MongoDB document

Given the `ExperienceEntry` class exists
When its persistence annotations are inspected
Then it is a Spring Data MongoDB document
And its `id` field is the Spring Data document id.

### Experience entry supports typed filtering fields

Given an `ExperienceEntry` is created
When `type`, `tags`, `technologies`, and date fields are set
Then those values can be read back from the model
And the shape supports future repository filtering by type, tag, and technology.

### Experience entry supports flexible facts

Given an `ExperienceEntry` is created
When `details` contains mixed and nested completed-entry facts
Then those facts are retained in `Map<String, Object>` form
And no AI-specific schema field is required.

### MVP boundaries remain intact

Given Sprint 02 is complete
When the model, dependencies, and package names are inspected
Then no AI, questionnaire, authentication, authorization, or client-facing user-management scope has been introduced.

## Coverage Expectations

- Line coverage should cover all `ExperienceType` enum access and all `ExperienceEntry` accessors.
- Branch coverage should cover null and non-null paths for `tags`, `technologies`, and `details` defaulting.
- Reflection tests should cover class/package placement, MongoDB annotations, required fields, and generic collection/map contracts.
- Boundary tests should cover forbidden AI/questionnaire/auth dependencies and packages.
- Do not treat Sprint 02 model tests as sufficient for Sprint 03 repository/service behavior; those need separate mock-based service tests.

## Remaining Risks

- Reflection tests can become brittle if they over-specify implementation details. Keep them tied to explicit sprint requirements.
- Model tests cannot prove MongoDB query behavior; repository filtering must be tested in Sprint 03.
- Default single-user assignment is intentionally deferred. Sprint 03 must add service tests proving the default user is assigned centrally before persistence.
