# Sprint 01: Foundation and Persistence Setup

## Goal

Prepare the backend foundation for MongoDB-backed experience-history storage while preserving the MVP boundaries.

## Ordered tasks

1. Configure MongoDB dependency
   - Source feature: `../../features/configure-mongodb-persistence.feature.md`
   - Add Spring Data MongoDB support to `pom.xml`.
   - Keep existing Spring Web MVC and test dependencies.

2. Configure MongoDB application properties
   - Source feature: `../../features/configure-mongodb-persistence.feature.md`
   - Keep `spring.application.name=cv-easy-maded`.
   - Require MongoDB configuration through `MONGODB_URI`.
   - Add database name configuration only if needed.

3. Keep tests independent from a live MongoDB instance
   - Source feature: `../../features/configure-mongodb-persistence.feature.md`
   - Ensure `./mvnw test` can run without manual MongoDB setup.
   - Avoid requiring a local default MongoDB URI in tests.

4. Create experience type enum
   - Source feature: `../../features/model-experience-entries.feature.md`
   - Create `ExperienceType.java` with all supported MVP values.

5. Create experience entry MongoDB document
   - Source feature: `../../features/model-experience-entries.feature.md`
   - Create `ExperienceEntry.java` under the `experience` package.
   - Include id, internal userId, type, title, summary, tags, technologies, details, occurredAt, createdAt, and updatedAt.

## Definition of done

- MongoDB support is configured without adding Docker, CI, auth, or AI dependencies.
- The initial domain model supports typed filters and flexible `details` data.
- Tests can still run without manual MongoDB setup.
