package com.vncrodrigues13.cv_easy_maded.experience;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("experience_entries")
public class ExperienceEntry {

	@Id
	private String id;

	private String userId;

	private ExperienceType type;

	private String title;

	private String summary;

	private List<String> tags = new ArrayList<>();

	private List<String> technologies = new ArrayList<>();

	private Map<String, Object> details = new LinkedHashMap<>();

	private Instant occurredAt;

	private Instant createdAt;

	private Instant updatedAt;

	public ExperienceEntry() {
	}

	public ExperienceEntry(String id, String userId, ExperienceType type, String title, String summary, List<String> tags,
			List<String> technologies, Map<String, Object> details, Instant occurredAt, Instant createdAt,
			Instant updatedAt) {
		this.id = id;
		this.userId = userId;
		this.type = type;
		this.title = title;
		this.summary = summary;
		setTags(tags);
		setTechnologies(technologies);
		setDetails(details);
		this.occurredAt = occurredAt;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public ExperienceType getType() {
		return type;
	}

	public void setType(ExperienceType type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags == null ? new ArrayList<>() : new ArrayList<>(tags);
	}

	public List<String> getTechnologies() {
		return technologies;
	}

	public void setTechnologies(List<String> technologies) {
		this.technologies = technologies == null ? new ArrayList<>() : new ArrayList<>(technologies);
	}

	public Map<String, Object> getDetails() {
		return details;
	}

	public void setDetails(Map<String, Object> details) {
		this.details = details == null ? new LinkedHashMap<>() : new LinkedHashMap<>(details);
	}

	public Instant getOccurredAt() {
		return occurredAt;
	}

	public void setOccurredAt(Instant occurredAt) {
		this.occurredAt = occurredAt;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
}
