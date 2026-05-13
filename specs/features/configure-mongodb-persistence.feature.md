# Feature: Configure MongoDB persistence

## Goal

Enable the Spring Boot backend to persist CV experience-history data in MongoDB.

## Context

The project is currently a fresh Spring Boot 4 / Java 21 backend. The MVP needs MongoDB because experience entries may contain flexible, semi-structured data.

## Scope

- Update `pom.xml` to include Spring Data MongoDB support.
- Keep the existing Spring Web MVC and test dependencies.
- Update `src/main/resources/application.properties` with MongoDB settings.
- Keep `spring.application.name=cv-easy-maded`.
- Configure MongoDB URI/database using environment-variable fallback, such as `MONGODB_URI` with a local default.

## Out of scope

- Docker Compose setup.
- Production secrets management.
- AI integration.
- Authentication or multi-user configuration.

## TODO

- [ ] Add the Spring Data MongoDB starter to `pom.xml`.
- [ ] Add MongoDB URI configuration to `application.properties`.
- [ ] Add database name configuration to `application.properties` if needed.
- [ ] Ensure the application can still start with local/default MongoDB settings.
- [ ] Ensure tests do not require a real MongoDB instance unless explicitly configured.

## Definition of done

- The project has MongoDB dependencies configured.
- MongoDB connection settings can be overridden through environment variables.
- Existing application configuration remains intact.
- `./mvnw test` can run without requiring manual MongoDB setup.
