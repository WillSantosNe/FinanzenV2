package com.finanzen.api.domain.outbox;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OutboxEvent {
    private Long id;
    private AggregateType aggregateType; // Ex: "TRANSACTION"
    private Long aggregateId;   // Ex: ID da Transacao (8)
    private EventType eventType;     // Ex: "TransactionCreatedEvent"
    private String payload;       // O JSON da transação
    private LocalDateTime createdAt;
    private boolean processed;

    public OutboxEvent(Long id, AggregateType aggregateType, Long aggregateId, EventType eventType, String payload, LocalDateTime createdAt, boolean processed) {
        this.id = id;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.payload = payload;
        this.createdAt = LocalDateTime.now();
        this.processed = false;
    }
}
