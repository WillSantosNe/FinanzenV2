package com.finanzen.api.adapters.out.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanzen.api.application.ports.out.user.UserEventPublisherPort;
import com.finanzen.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaProducerAdapter implements UserEventPublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "user-created";

    @Override
    public void publishUserCreated(User user) {
        try {
            String json = objectMapper.writeValueAsString(user);
            String messageKey = user.getId() != null ? user.getId().toString() : null;
            kafkaTemplate.send(TOPIC, messageKey, json);
        }catch (Exception e){
            throw new RuntimeException("Failed to serialize and publish user event to Apache Kafka", e);
        }
    }
}
