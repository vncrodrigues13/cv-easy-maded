package com.vncrodrigues13.cv_easy_maded;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.w3c.dom.Element;

class ConfigurationPropertiesTest {

	@Test
	void applicationPropertiesRequireMongoUriFromEnvironmentWithoutLocalFallback() throws Exception {
		Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource("application.properties"));

		assertThat(properties.getProperty("spring.application.name")).isEqualTo("cv-easy-maded");
		assertThat(properties.getProperty("spring.mongodb.uri")).isEqualTo("${MONGODB_URI}");
		assertThat(properties).doesNotContainKey("spring.mongodb.database");

		String rawProperties = Files.readString(Path.of("src/main/resources/application.properties"));
		assertThat(rawProperties).doesNotContain("mongodb://localhost", "127.0.0.1", "${MONGODB_URI:");
	}

	@Test
	void pomKeepsRequiredStartersAndDoesNotAddOutOfScopeDependencies() throws Exception {
		List<String> artifactIds = dependencyArtifactIds();

		assertThat(artifactIds).contains("spring-boot-starter-webmvc", "spring-boot-starter-webmvc-test",
				"spring-boot-starter-data-mongodb");
		assertThat(artifactIds).doesNotContain("spring-boot-starter-security");
		assertThat(artifactIds).noneMatch(artifactId -> artifactId.toLowerCase().contains("openai"));
		assertThat(artifactIds).noneMatch(artifactId -> artifactId.toLowerCase().contains("claude"));
		assertThat(artifactIds).noneMatch(artifactId -> artifactId.toLowerCase().contains("ai"));
	}

	@Test
	void repositoryDoesNotIntroduceOutOfScopeFilesOrPackages() throws Exception {
		List<String> paths;
		try (var stream = Files.walk(Path.of("."))) {
			paths = stream.filter(Files::isRegularFile).map(Path::normalize).map(Path::toString).toList();
		}

		assertThat(paths).noneMatch(path -> path.equals("./Dockerfile") || path.endsWith("/Dockerfile")
				|| path.endsWith("docker-compose.yml") || path.contains("/.github/workflows/")
				|| path.contains("/deployment/") || path.contains("/deploy/"));
		assertThat(paths).noneMatch(path -> path.contains("/src/main/java/") && (path.contains("/auth/")
				|| path.contains("/authentication/") || path.contains("/authorization/")
				|| path.contains("/questionnaire/") || path.contains("/ai/")));
	}

	private static List<String> dependencyArtifactIds() throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);

		try (InputStream inputStream = Files.newInputStream(Path.of("pom.xml"))) {
			Element root = factory.newDocumentBuilder().parse(inputStream).getDocumentElement();
			var artifactIdNodes = root.getElementsByTagName("artifactId");
			return java.util.stream.IntStream.range(0, artifactIdNodes.getLength())
				.mapToObj(index -> artifactIdNodes.item(index).getTextContent())
				.toList();
		}
	}
}
