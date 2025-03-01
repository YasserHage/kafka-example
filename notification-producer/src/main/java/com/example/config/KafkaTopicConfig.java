package com.example.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topics.notification.name}")
    private String topicName;

    @Value("${kafka.topics.notification.partitions}")
    private int partitions;

    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder
                .name(topicName)
                .partitions(partitions)
                .build();
    }
}
