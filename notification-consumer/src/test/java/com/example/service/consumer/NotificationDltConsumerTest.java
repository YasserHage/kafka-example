package com.example.service.consumer;

import com.example.config.KafkaIntegrationTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@ExtendWith(SpringExtension.class)
public class NotificationDltConsumerTest extends KafkaIntegrationTest {

    @Value("${kafka.topics.notification.name}")
    private String topicName;

    @Autowired
    NotificationConsumer consumer;

    @MockitoSpyBean
    NotificationDltConsumer dltConsumer;

    @Test
    public void testConsumerReceiveErrorMessage() {
        String key = NotificationConsumer.ERROR_KEY;
        String message = "Test Kafka";
        kafkaTemplate.send(topicName, key, message);

        await().atMost(5, TimeUnit.SECONDS)
                .untilAsserted(() -> verify(dltConsumer).process(any()));
    }
}
