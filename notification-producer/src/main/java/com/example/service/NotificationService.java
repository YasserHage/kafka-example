package com.example.service;

import com.example.model.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Value("${kafka.topics.notification.name}")
    private String topicName;

    public void sendMessage(Notification notification) {
        kafkaTemplate.send(topicName, notification.getId(), notification.getMessage());
        log.info("Message sent to topic: {}, Id: {}, Message: {}.",
                topicName, notification.getId(), notification.getMessage());
    }

    public void sendBulkMessages(Integer repeat, String message) {
        for (int i = 1; i <= 100; i++) {
            for (int key = 1; key <= repeat; key++) {
                kafkaTemplate.send(topicName, String.valueOf(key), message + " (" + i + ")");
            }
        }
        log.info("Bulk messages sent to topic: {}, Repeat for {} IDs, Message: {}.",
                topicName, repeat, message);
    }
}
