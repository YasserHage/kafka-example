package com.example.service.consumer;

import com.example.config.exception.NotificationProcessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.retrytopic.TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE;

@Slf4j
@Service
public class NotificationConsumer {

    public static final String ERROR_KEY = "error-test";

    @RetryableTopic(attempts = "3",
            backoff = @Backoff(delay = 1000, multiplier = 2),
            include = {NotificationProcessException.class},
            topicSuffixingStrategy = SUFFIX_WITH_INDEX_VALUE)
    @KafkaListener(topics = "${kafka.topics.notification.name}",
            groupId = "notification-consumer",
            concurrency = "${kafka.topics.notification.partitions}")
    public void process(ConsumerRecord<String, String> record) {

        if (record.key().equals(ERROR_KEY)) {
            log.warn("Processing error message: {}", record.value());
            throw new NotificationProcessException("Error processing message: " + record.value());
        }

        log.info("Received notification for Id: {} -> {}", record.key(), record.value());
    }
}
