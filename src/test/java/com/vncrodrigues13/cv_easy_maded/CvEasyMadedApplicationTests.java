package com.vncrodrigues13.cv_easy_maded;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.autoconfigure.exclude=org.springframework.boot.mongodb.autoconfigure.MongoAutoConfiguration,org.springframework.boot.data.mongodb.autoconfigure.DataMongoAutoConfiguration" })
class CvEasyMadedApplicationTests {

	@Test
	void contextLoads() {
	}

}
