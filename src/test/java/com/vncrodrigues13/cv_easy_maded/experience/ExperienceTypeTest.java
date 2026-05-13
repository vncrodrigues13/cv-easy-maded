package com.vncrodrigues13.cv_easy_maded.experience;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExperienceTypeTest {

	@Test
	void containsExactlyTheMvpExperienceTypes() {
		assertThat(ExperienceType.values()).containsExactly(ExperienceType.PROJECT, ExperienceType.TOOL,
				ExperienceType.PROGRAMMING_LANGUAGE, ExperienceType.CHALLENGE, ExperienceType.BOOK,
				ExperienceType.ARTICLE, ExperienceType.PAPER, ExperienceType.OTHER);
	}
}
