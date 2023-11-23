package com.tfl.codingchallenge;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CodingChallengeApplicationITest {

	@Test
	void contextLoads(ApplicationContext applicationContext) {
		assertThat(applicationContext).isNotNull();
	}

	@Test
	void contextTest(ApplicationContext applicationContext) {
			CodingChallengeApplication.main(new String[] {});
		}
}
