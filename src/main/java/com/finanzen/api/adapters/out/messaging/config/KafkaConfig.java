package com.finanzen.api.adapters.out.messaging.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for Apache Kafka infrastructure.
 * Ensures the necessary topics are created at startup.
 */
@Configuration
public class KafkaConfig {

    /**
     * Creates the topic for transaction events if it does not already exist.
     *
     * @return a NewTopic bean managed by the Spring context.
     */
    @Bean
    public NewTopic transactionEventsTopic() {
        TopicBuilder topicBuilder = TopicBuilder.name("transaction-events");
        return topicBuilder.partitions(1).replicas(1).build();
    }
}
