# Plan: CV experience-history backend MVP

## Context

The project is currently a fresh Spring Boot 4 / Java 21 backend with only the generated application class, one context-load test, and no domain/API/storage code yet. The goal is to start a personal CV generator/history system where one user can periodically store professional experience information: projects, tools, programming languages, challenges, books, posts, articles, and papers. For the first implementation, target a single-user MVP, use MongoDB for flexible/unstructured documents, and skip AI integration for now.

## Recommended approach

Build a backend-only MVP that stores and retrieves experience entries in MongoDB through a small REST API. Keep each entry semi-structured: a few searchable/common fields plus a flexible `details` map for arbitrary information. This gives the app useful persistence now without forcing a final schema before the product shape is known.

## Files to modify/create

### Project planning document

- Create `plans/cv-experience-history-backend-mvp.md`
  - Save this approved plan inside the project repository before implementation work begins.

### Build and configuration

- Modify `pom.xml`
  - Add Spring Data MongoDB starter.
  - Keep current Spring Web MVC and test dependencies.
- Modify `src/main/resources/application.properties`
  - Keep `spring.application.name=cv-easy-maded`.
  - Add MongoDB URI/database configuration using environment-variable fallback, e.g. `MONGODB_URI` with local default.

### Domain/API package layout

Create files under `src/main/java/com/vncrodrigues13/cv_easy_maded/experience/`:

- `ExperienceEntry.java`
  - MongoDB document model.
  - Suggested fields:
    - `id`
    - `userId` defaulting to a constant single-user value for now
    - `type` enum: `PROJECT`, `TOOL`, `PROGRAMMING_LANGUAGE`, `CHALLENGE`, `BOOK`, `ARTICLE`, `PAPER`, `OTHER`
    - `title`
    - `summary`
    - `tags`
    - `technologies`
    - `details` as `Map<String, Object>` for unstructured data
    - `occurredAt`
    - `createdAt`
    - `updatedAt`
- `ExperienceType.java`
  - Enum for entry categories.
- `ExperienceEntryRepository.java`
  - Extends Spring Data Mongo repository.
  - Query methods for single-user listing and optional filtering by type/tag.
- `ExperienceEntryService.java`
  - Owns single-user defaulting, timestamp handling, validation orchestration, and repository calls.
- `ExperienceEntryController.java`
  - REST endpoints for creating, listing, reading, updating, and deleting entries.
- DTOs as records in the same package or `experience/dto/`:
  - `CreateExperienceEntryRequest`
  - `UpdateExperienceEntryRequest`
  - `ExperienceEntryResponse`

### Tests

Create/update tests under `src/test/java/com/vncrodrigues13/cv_easy_maded/experience/`:

- `ExperienceEntryServiceTest.java`
  - Unit-test timestamp/default-user behavior and repository interactions.
- `ExperienceEntryControllerTest.java`
  - Use Spring MVC test support to verify request/response behavior for create/list/get/update/delete.
- Keep/update `CvEasyMadedApplicationTests.java`
  - Ensure app context still loads with Mongo configuration present.

## REST API shape

Base path: `/api/experience-entries`

- `POST /api/experience-entries`
  - Creates one entry.
  - Supports adding information one by one.
- `POST /api/experience-entries/bulk`
  - Creates a package of entries in one request.
  - Useful when importing several tools/books/articles/challenges at once.
- `GET /api/experience-entries`
  - Lists entries for the single user.
  - Optional filters: `type`, `tag`, `technology`.
- `GET /api/experience-entries/{id}`
  - Reads one entry.
- `PUT /api/experience-entries/{id}`
  - Replaces editable entry fields.
- `DELETE /api/experience-entries/{id}`
  - Removes an entry.

## Data model notes

Keep common fields typed enough for future CV generation and search, but leave specific experience facts in `details`:

```json
{
  "type": "PROJECT",
  "title": "OAuth protocol application",
  "summary": "Implemented an OAuth-based authentication flow.",
  "tags": ["authentication", "security"],
  "technologies": ["Java", "Spring Boot", "OAuth"],
  "details": {
    "challenge": "Understanding token exchange and redirect flows",
    "role": "Backend developer",
    "outcome": "Completed working OAuth login flow"
  },
  "occurredAt": "2026-05-12"
}
```

This structure keeps MongoDB useful for flexible storage while still allowing future filtering and CV generation.

## AI integration later

Do not wire OpenAI in the MVP. Leave the design ready for a later `questionnaire` or `ai` package that can:

1. Accept a draft entry.
2. Generate follow-up questions.
3. Collect answers.
4. Enrich and save the final `ExperienceEntry`.

For now, users directly submit completed entry data through the REST API.

## Verification

After implementation:

1. Run `./mvnw test`.
2. Start the app with local MongoDB configured via `MONGODB_URI` or the default local URI.
3. Manually verify with HTTP requests:
   - create one project entry,
   - create a bulk package of mixed entries,
   - list and filter entries,
   - update one entry,
   - delete one entry.
4. Confirm MongoDB contains the saved documents with flexible `details` fields.
