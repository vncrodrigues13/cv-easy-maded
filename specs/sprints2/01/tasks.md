# Sprint 01: Foundation and Persistence Setup

## Goal

Prepare the backend foundation for MongoDB-backed experience-history storage while preserving MVP boundaries and creating the initial experience-entry domain model.

## Source Features

- `../../features/configure-mongodb-persistence.feature.md`
- `../../features/model-experience-entries.feature.md`
- `../../features/defer-ai-entry-enrichment.feature.md`

## Scope Preservation Note

This sprint preserves the valid scope from the original Sprint 01: MongoDB persistence setup and the initial experience-entry model. No supported feature work is removed. The MongoDB URI behavior is narrowed only where the source feature conflicts with `CLAUDE.md`.

## Ordered Tasks

- [X] Add Spring Data MongoDB support to `pom.xml` while keeping the existing Spring Web MVC and test dependencies.
- [X] Configure MongoDB in `src/main/resources/application.properties`, keeping `spring.application.name=cv-easy-maded` and requiring `MONGODB_URI` as the primary connection source.
- [X] Do not provide an implicit local fallback MongoDB URI in application configuration.
- [X] Add database-name configuration only if the project needs it separately from the URI.
- [X] Ensure `./mvnw test` can run without a live MongoDB instance by avoiding eager repository/database usage in default test paths.
- [X] Create `ExperienceType.java` under the `experience` package with the supported MVP values: `PROJECT`, `TOOL`, `PROGRAMMING_LANGUAGE`, `CHALLENGE`, `BOOK`, `ARTICLE`, `PAPER`, and `OTHER`.
- [X] Create `ExperienceEntry.java` as a Spring Data MongoDB document under the `experience` package.
- [X] Include `id`, internal `userId`, `type`, `title`, `summary`, `tags`, `technologies`, `details`, `occurredAt`, `createdAt`, and `updatedAt` fields in `ExperienceEntry`.
- [X] Represent `details` as flexible `Map<String, Object>` data for entry-specific facts and future enriched facts.
- [X] Represent `tags` and `technologies` as list-like fields for future filtering.
- [X] Keep the single-user assumption internal to backend domain/service logic and do not expose user-management behavior.
- [X] Verify no AI, questionnaire, authentication, Docker, deployment, or CI dependencies/configuration are introduced.

## Tech Lead Decisions

- Follow `CLAUDE.md` over the source feature wording for MongoDB URI behavior.
- Configure MongoDB with `MONGODB_URI` and do not provide an implicit local fallback URI.
- Tests must avoid requiring a live MongoDB instance by using mocks, test overrides, or another explicit test-only strategy.
- Skip separate database-name configuration unless implementation proves it is needed, because the database can be provided in `MONGODB_URI`.
- Keep `ExperienceType` and `ExperienceEntry` in Sprint 01 because they are supported by `CLAUDE.md`, the original sprint, and `model-experience-entries.feature.md`.

## Definition of Done

- MongoDB dependencies and configuration are present.
- MongoDB configuration can be overridden through `MONGODB_URI`.
- The application keeps `spring.application.name=cv-easy-maded`.
- `./mvnw test` is expected to run without manual MongoDB setup.
- The initial domain model supports typed filters through common fields and flexible facts through `details`.
- The MVP remains backend-only and introduces no AI, questionnaire, authentication, Docker, deployment, or CI scope.
