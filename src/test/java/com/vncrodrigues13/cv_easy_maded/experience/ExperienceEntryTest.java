package com.vncrodrigues13.cv_easy_maded.experience;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

class ExperienceEntryTest {

	@Test
	void livesInExperiencePackage() {
		assertThat(ExperienceEntry.class.getPackageName())
			.isEqualTo("com.vncrodrigues13.cv_easy_maded.experience");
	}

	@Test
	void isMongoDocumentWithSpringDataId() throws Exception {
		assertThat(ExperienceEntry.class).hasAnnotation(Document.class);
		assertThat(ExperienceEntry.class.getDeclaredField("id").getAnnotation(Id.class)).isNotNull();
	}

	@Test
	void hasRequiredFieldsWithExpectedTypes() throws Exception {
		assertThat(ExperienceEntry.class.getDeclaredField("id").getType()).isEqualTo(String.class);
		assertThat(ExperienceEntry.class.getDeclaredField("userId").getType()).isEqualTo(String.class);
		assertThat(ExperienceEntry.class.getDeclaredField("type").getType()).isEqualTo(ExperienceType.class);
		assertThat(ExperienceEntry.class.getDeclaredField("title").getType()).isEqualTo(String.class);
		assertThat(ExperienceEntry.class.getDeclaredField("summary").getType()).isEqualTo(String.class);
		assertThat(ExperienceEntry.class.getDeclaredField("tags").getType()).isEqualTo(List.class);
		assertThat(ExperienceEntry.class.getDeclaredField("technologies").getType()).isEqualTo(List.class);
		assertThat(ExperienceEntry.class.getDeclaredField("details").getType()).isEqualTo(Map.class);
		assertThat(ExperienceEntry.class.getDeclaredField("occurredAt").getType()).isEqualTo(Instant.class);
		assertThat(ExperienceEntry.class.getDeclaredField("createdAt").getType()).isEqualTo(Instant.class);
		assertThat(ExperienceEntry.class.getDeclaredField("updatedAt").getType()).isEqualTo(Instant.class);

		assertListOfStringField("tags");
		assertListOfStringField("technologies");
		assertMapOfStringToObjectField("details");
	}

	@Test
	void roundTripsAllFieldsThroughAccessors() {
		Instant occurredAt = Instant.parse("2024-01-15T10:00:00Z");
		Instant createdAt = Instant.parse("2024-01-16T10:00:00Z");
		Instant updatedAt = Instant.parse("2024-01-17T10:00:00Z");
		Map<String, Object> details = Map.of("impact", "reduced review time");

		ExperienceEntry entry = new ExperienceEntry();
		entry.setId("entry-1");
		entry.setUserId("default-user");
		entry.setType(ExperienceType.PROJECT);
		entry.setTitle("CV history API");
		entry.setSummary("Built the first MVP backend");
		entry.setTags(List.of("career", "backend"));
		entry.setTechnologies(List.of("Java", "MongoDB"));
		entry.setDetails(details);
		entry.setOccurredAt(occurredAt);
		entry.setCreatedAt(createdAt);
		entry.setUpdatedAt(updatedAt);

		assertThat(entry.getId()).isEqualTo("entry-1");
		assertThat(entry.getUserId()).isEqualTo("default-user");
		assertThat(entry.getType()).isEqualTo(ExperienceType.PROJECT);
		assertThat(entry.getTitle()).isEqualTo("CV history API");
		assertThat(entry.getSummary()).isEqualTo("Built the first MVP backend");
		assertThat(entry.getTags()).containsExactly("career", "backend");
		assertThat(entry.getTechnologies()).containsExactly("Java", "MongoDB");
		assertThat(entry.getDetails()).containsExactlyEntriesOf(details);
		assertThat(entry.getOccurredAt()).isEqualTo(occurredAt);
		assertThat(entry.getCreatedAt()).isEqualTo(createdAt);
		assertThat(entry.getUpdatedAt()).isEqualTo(updatedAt);
	}

	@Test
	void preservesFlexibleDetailsAndListLikeFields() {
		Map<String, Object> nested = Map.of("impact", "reduced manual work", "visible", true);
		Map<String, Object> details = Map.of("role", "backend", "years", 2, "remote", true, "topics",
				List.of("spring", "mongodb"), "nested", nested);

		ExperienceEntry entry = new ExperienceEntry();
		entry.setTags(List.of("career", "mvp"));
		entry.setTechnologies(List.of("Java", "MongoDB"));
		entry.setDetails(details);

		assertThat(entry.getTags()).containsExactly("career", "mvp");
		assertThat(entry.getTechnologies()).containsExactly("Java", "MongoDB");
		assertThat(entry.getDetails()).containsEntry("role", "backend").containsEntry("years", 2)
			.containsEntry("remote", true)
			.containsEntry("topics", List.of("spring", "mongodb"))
			.containsEntry("nested", nested);
	}

	@Test
	void defaultsCollectionFieldsToEmptyInsteadOfNull() {
		ExperienceEntry entry = new ExperienceEntry();

		assertThat(entry.getTags()).isEmpty();
		assertThat(entry.getTechnologies()).isEmpty();
		assertThat(entry.getDetails()).isEmpty();

		entry.setTags(null);
		entry.setTechnologies(null);
		entry.setDetails(null);

		assertThat(entry.getTags()).isEmpty();
		assertThat(entry.getTechnologies()).isEmpty();
		assertThat(entry.getDetails()).isEmpty();
	}

	@Test
	void copiesCollectionsAndDetailsWhenSettingValues() {
		List<String> tags = new ArrayList<>(List.of("career"));
		List<String> technologies = new ArrayList<>(List.of("Java"));
		Map<String, Object> details = new LinkedHashMap<>(Map.of("role", "backend"));

		ExperienceEntry entry = new ExperienceEntry();
		entry.setTags(tags);
		entry.setTechnologies(technologies);
		entry.setDetails(details);

		tags.add("mutated");
		technologies.add("mutated");
		details.put("mutated", true);

		assertThat(entry.getTags()).containsExactly("career");
		assertThat(entry.getTechnologies()).containsExactly("Java");
		assertThat(entry.getDetails()).containsExactly(Map.entry("role", "backend"));
	}

	@Test
	void doesNotExposeAiQuestionnaireOrClientUserManagementFields() {
		assertThat(List.of(ExperienceEntry.class.getDeclaredFields()).stream().map(Field::getName).toList())
			.contains("userId")
			.doesNotContain("username", "email", "owner", "tenantId", "prompt", "questions", "answers",
					"enrichment", "provider", "model", "llm");
	}

	private static void assertListOfStringField(String fieldName) throws Exception {
		ParameterizedType type = (ParameterizedType) ExperienceEntry.class.getDeclaredField(fieldName)
			.getGenericType();

		assertThat(type.getActualTypeArguments()).containsExactly(String.class);
	}

	private static void assertMapOfStringToObjectField(String fieldName) throws Exception {
		ParameterizedType type = (ParameterizedType) ExperienceEntry.class.getDeclaredField(fieldName)
			.getGenericType();

		assertThat(type.getActualTypeArguments()).containsExactly(String.class, Object.class);
	}
}
