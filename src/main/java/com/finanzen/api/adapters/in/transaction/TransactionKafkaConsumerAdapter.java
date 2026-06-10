package com.finanzen.api.adapters.in.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanzen.api.domain.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TransactionKafkaConsumerAdapter {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "transaction-created", groupId = "finanzen-group")
    public void consumeTransactionCreated(String payload){
        try {
            Transaction transaction = objectMapper.readValue(payload, Transaction.class);

            // Simulando processamento de disparo de email
            System.out.println("[EMAIL SERVICE] -> Preparing financial alert notification...");

            System.out.println("Sending email to: " +  transaction.getUserEmail());

            System.out.println("Message Content: Hi! Your transaction for " +  transaction.getDescription()
                    + " of U$ " + transaction.getAmount() + " was successfully processed!");

            System.out.println("[EMAIL SERVICE] -> Notification delivered successfully for Transaction ID: "
                    +  transaction.getId());
        } catch (Exception e) {
            // Garante que um erro de conversão não derrube o container de escuta do Kafka
            System.out.println("Error while deserializing or processing transaction event from Kafka - " + e);
        }
    }
}
