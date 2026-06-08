package com.finanzen.api.adapters.out.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanzen.api.application.ports.out.transaction.TransactionEventPublisherPort;
import com.finanzen.api.domain.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionKafkaProducerAdapter implements TransactionEventPublisherPort {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private static final String TOPIC = "transaction-created";

    @Override
    public void publishTransactionCreated(Transaction transaction) {
        try{
            // Convertendo entidade para uma linha JSON
            String json = objectMapper.writeValueAsString(transaction);

            // Mandando para Kafka usando o id do Transaction como chave de mensagem
            String messageKey = transaction.getId() != null ? transaction.getId().toString() : null;

            kafkaTemplate.send(TOPIC, messageKey, json);

        }catch(Exception e){
            throw new RuntimeException("Failed to serialize and publish transaction event to Apache Kafka", e);
        }
    }
}
