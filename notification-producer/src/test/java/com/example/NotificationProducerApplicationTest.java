package com.example;

import com.example.config.KafkaIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext
class NotificationProducerApplicationTest extends KafkaIntegrationTest {

	@Test
	void contextLoads() {
	}

}
