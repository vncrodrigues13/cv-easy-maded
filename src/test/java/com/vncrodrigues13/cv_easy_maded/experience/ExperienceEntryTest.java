package com.vncrodrigues13.cv_easy_maded.experience;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.ParameterizedType;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

class ExperienceEntryTest {

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
