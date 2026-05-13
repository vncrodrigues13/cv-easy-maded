# Sprint 02: Repository and Service Core

## Goal

Implement persistence access and single-user service behavior for experience entries.

## Ordered tasks

1. Create experience entry repository
   - Source feature: `../../features/experience-repository-service.feature.md`
   - Create `ExperienceEntryRepository.java` extending the Spring Data MongoDB repository type.
   - Add query methods scoped to the default MVP user.

2. Add repository filtering methods
   - Source feature: `../../features/experience-repository-service.feature.md`
   - Support listing by default user.
   - Support optional filters for type, tag, and technology.

3. Create experience entry service
   - Source feature: `../../features/experience-repository-service.feature.md`
   - Create `ExperienceEntryService.java`.
   - Centralize the default single-user constant and user assignment in service/domain logic.

4. Implement create and validation behavior
   - Source feature: `../../features/experience-repository-service.feature.md`
   - Implement single create.
   - Require type and non-blank title.
   - Safely default tags, technologies, and details to empty values.
   - Set createdAt and updatedAt.

5. Implement service read operations
   - Source feature: `../../features/experience-repository-service.feature.md`
   - Implement list with optional filters.
   - Implement get by ID scoped to the default user.

## Definition of done

- Repository methods support MVP lookup and filter needs.
- Service create/list/get behavior works for the default single user.
- Required-field validation and timestamp creation handling are in place.
