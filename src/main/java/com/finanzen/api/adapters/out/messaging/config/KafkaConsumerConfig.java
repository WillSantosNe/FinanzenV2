package com.finanzen.api.adapters.out.messaging.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {
    @Bean
    public DefaultErrorHandler errorHandler(KafkaTemplate<String, String> kafkaTemplate) {

        // Recoverer - cria tópico com .DLQ para ser tratado posteriormente e seguir a fila do tópico normalmente
        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate,
                (record, exception) -> new TopicPartition(record.topic() + ".DLQ", record.partition()));

        // Backoff - retentativas
        FixedBackOff backOff = new FixedBackOff(1000L, 2);

        // Retorna DefaultErrorHandler
        return new DefaultErrorHandler(recoverer, backOff);
    }
}
