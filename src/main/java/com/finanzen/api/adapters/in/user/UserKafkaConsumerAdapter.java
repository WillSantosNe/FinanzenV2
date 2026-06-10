package com.finanzen.api.adapters.in.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanzen.api.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserKafkaConsumerAdapter {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user-created", groupId = "finanzen-group")
    public void consumeUserCreated(String payload){
        try{
            User user = objectMapper.readValue(payload, User.class);

            // Simulando processamento de disparo de email
            System.out.println("[EMAIL SERVICE] -> Preparing financial alert notification...");

            System.out.println("Sending email to: " +  user.getEmail());

            System.out.println("Message Content: Hi! Your user for was successfully processed!");

            System.out.println("[EMAIL SERVICE] -> Notification delivered successfully for User ID: "
                    +  user.getId());
        } catch (Exception e) {
            // Garante que um erro de conversão não derrube o container de escuta do Kafka
            System.out.println("Error while deserializing or processing user event from Kafka - " + e);
        }
    }

}
