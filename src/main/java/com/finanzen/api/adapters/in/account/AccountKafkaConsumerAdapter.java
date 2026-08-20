package com.finanzen.api.adapters.in.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanzen.api.domain.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountKafkaConsumerAdapter {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "transaction-events", groupId = "finanzen-group")
    public void consumeTransactionCreated(String payload) {
        try {
            Transaction transaction = objectMapper.readValue(payload, Transaction.class);

            // Notificando
            System.out.println("[NOTIFICATION SERVICE] -> Sending confirmation for Transaction: " + transaction.getId()
                    + " | Amount: " + transaction.getAmount()
                    + " | User: " + transaction.getUserEmail());

        } catch (Exception e) {
            throw new RuntimeException("Error processing transaction event for notification", e);
        }
    }
}