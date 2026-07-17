package com.finanzen.api.adapters.out.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanzen.api.application.ports.out.OutboxRepositoryPort;
import com.finanzen.api.application.ports.out.transaction.TransactionEventPublisherPort;
import com.finanzen.api.domain.outbox.AggregateType;
import com.finanzen.api.domain.outbox.EventType;
import com.finanzen.api.domain.outbox.OutboxEvent;
import com.finanzen.api.domain.transaction.Transaction;
import com.finanzen.api.utils.mapper.OutboxMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionOutboxAdapter implements TransactionEventPublisherPort {

    private final ObjectMapper objectMapper;
    private final OutboxRepositoryPort repositoryPort;

    @Override
    public void publishTransactionCreated(Transaction transaction) {
        try {
            String json = objectMapper.writeValueAsString(transaction);

            OutboxEvent event = new OutboxEvent(
                    null,
                    AggregateType.TRANSACTION,
                    transaction.getId(),
                    EventType.TRANSACTION_CREATED_EVENT,
                    json,
                    null,
                    false
                    );

            OutboxEvent outboxEvent = repositoryPort.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize and publish transaction event to OutboxEvents", e);
        }



    }
}
