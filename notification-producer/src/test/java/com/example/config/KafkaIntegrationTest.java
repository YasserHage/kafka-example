package com.example.config;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.BeforeClass;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.kafka.KafkaContainer;

import java.util.Map;
import java.util.Properties;
import java.util.UUID;

public abstract class KafkaIntegrationTest {

    protected static AdminClient adminClient;
    protected static KafkaConsumer<String, String> kafkaConsumer;
    static KafkaContainer kafkaContainer = new KafkaContainer("apache/kafka-native:3.8.0");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }

    @BeforeClass
    public static void beforeAll() {
        kafkaContainer.start();
        KafkaAdmin kafkaAdmin = new KafkaAdmin(Map.of(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaContainer.getBootstrapServers()));
        adminClient = AdminClient.create(kafkaAdmin.getConfigurationProperties());
        kafkaConsumer = new KafkaConsumer<>(getProps());
    }

    protected static Properties getProps() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group-" + UUID.randomUUID());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "500");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }
}
