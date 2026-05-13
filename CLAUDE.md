# CLAUDE.md

## Project Overview

This project is a backend-only Spring Boot 4 / Java 21 MVP for storing personal CV experience-history entries.

The initial product goal is a single-user system where professional experience information can be stored over time, including projects, tools, programming languages, challenges, books, posts, articles, and papers.

## Project Planning

- Features and sprints are defined in the `specs/` folder.

## Build and Test

- Use the Maven wrapper instead of system Maven.
- Run the test suite with:

```bash
./mvnw test
```
- Prefer unit-first tests for the MVP. Mock repositories/services for fast service and controller coverage; add real MongoDB integration tests only when explicitly needed.

## Architecture Guidance

- Keep the MVP backend-only unless frontend work is explicitly requested.
- Use MongoDB for persistence.
- Use Spring Data MongoDB for repository/data-access code.
- Keep experience-history domain/API code under an `experience` package.
- Model experience entries as semi-structured documents:
  - typed common/searchable fields such as type, title, summary, tags, technologies, and dates;
  - flexible unstructured facts in a `details` map.
- Keep the current single-user assumption centralized in service/domain logic instead of spreading user defaults through controllers.
- Prefer simple Spring MVC and Spring Data patterns over extra abstractions.

## API Guidance

- Use `/api/experience-entries` as the base REST path for experience-entry endpoints.
- The intended MVP API shape includes:
  - create one entry;
  - bulk-create entries;
  - list entries with optional filters such as type, tag, or technology;
  - get one entry by id;
  - update one entry;
  - delete one entry.
- Treat bulk create as required for the MVP, not optional future work.
- Use minimal request validation for the MVP: require essential fields such as type and title, while keeping `details` flexible.

## MVP Constraints

- Do not add OpenAI, Claude, or other AI integration in the MVP unless explicitly requested.
- Do not introduce authentication or authorization unless explicitly requested.
- Do not assume a final CV-generation schema yet.
- Do not add Docker, deployment, or CI configuration unless explicitly requested.
- Avoid speculative abstractions for future multi-user or AI flows.

## Configuration Guidance

- Keep `spring.application.name=cv-easy-maded`.
- Require MongoDB configuration through `MONGODB_URI`; do not rely on an implicit local default URI.

## Verification

- Run `./mvnw test` after backend changes.
- For persistence changes, verify the app can use MongoDB through `MONGODB_URI`.
- When manually checking the API, cover create, bulk create, list/filter, get, update, and delete flows.
