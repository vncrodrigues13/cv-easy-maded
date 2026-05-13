# Feature: Model experience entries

## Goal

Create the domain model for storing professional experience-history entries in MongoDB.

## Context

The MVP stores experience information such as projects, tools, programming languages, challenges, books, articles, and papers. The model should be typed enough for filtering and future CV generation, while allowing arbitrary details through MongoDB.

## Scope

Create domain files under `src/main/java/com/vncrodrigues13/cv_easy_maded/experience/`.

The main MongoDB document should include:

- `id`
- `userId`, defaulting to a single-user constant for now
- `type`
- `title`
- `summary`
- `tags`
- `technologies`
- `details` as `Map<String, Object>`
- `occurredAt`
- `createdAt`
- `updatedAt`

Supported types:

- `PROJECT`
- `TOOL`
- `PROGRAMMING_LANGUAGE`
- `CHALLENGE`
- `BOOK`
- `ARTICLE`
- `PAPER`
- `OTHER`

## Out of scope

- User accounts.
- Authentication.
- AI enrichment.
- Full-text search indexes.

## TODO

- [ ] Create `ExperienceType.java` with the supported enum values.
- [ ] Create `ExperienceEntry.java` as a MongoDB document.
- [ ] Add fields for typed filtering and flexible `details` data.
- [ ] Add timestamp fields for creation and update tracking.
- [ ] Represent tags and technologies as list-like fields.
- [ ] Keep `userId` internal to the backend and defaulted for the single-user MVP.

## Definition of done

- Experience entries can represent all planned MVP categories.
- Flexible entry-specific data can be stored in `details`.
- The model supports future filtering by type, tag, and technology.
- The model is compatible with Spring Data MongoDB.
