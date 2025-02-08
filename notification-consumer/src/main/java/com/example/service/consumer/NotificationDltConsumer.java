package com.example.service.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationDltConsumer {

    @KafkaListener(topics = "#{'${kafka.topics.notification.name}' + '-dlt'}",
            groupId = "notification-dlt-consumer")
    public void process(ConsumerRecord<String, String> record) {
        log.warn("Processing notification failed for Id: {} -> {}", record.key(), record.value());
    }
}
