package com.example.service;

import com.example.config.KafkaIntegrationTest;
import com.example.model.Notification;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@ExtendWith(SpringExtension.class)
public class NotificationServiceTest extends KafkaIntegrationTest {

    @Autowired
    NotificationService service;

    private NewTopic topic;

    @BeforeEach
    public void before() throws ExecutionException, InterruptedException {
        topic = TopicBuilder.name("test-topic-" + UUID.randomUUID()).build();
        adminClient.createTopics(Collections.singletonList(topic)).all().get();
        service.setTopicName(topic.name());
    }

    @Test
    public void testProducerSendMessage() {
        String key = "1";
        String message = "Test Kafka";
        service.sendMessage(new Notification(key, message));

        kafkaConsumer.subscribe(Collections.singletonList(topic.name()));
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1));

        assertThat(records.count()).isEqualTo(1);
        records.forEach(record -> {
            assertThat(record.key()).isEqualTo(key);
            assertThat(record.value()).isEqualTo(message);
        });
    }

    @Test
    public void testProducerSendBulkMessages() {
        Integer repeat = 2;
        String message = "Test Kafka";

        service.sendBulkMessages(2, message);
        kafkaConsumer.subscribe(Collections.singletonList(topic.name()));
        ConsumerRecords<String, String> records = kafkaConsumer.poll(Duration.ofSeconds(1));

        assertThat(records.count()).isEqualTo(200);
        records.forEach(record -> {
            assertThat(Integer.valueOf(record.key())).isLessThanOrEqualTo(repeat);
            assertTrue(record.value().contains(message));
        });
    }
}
