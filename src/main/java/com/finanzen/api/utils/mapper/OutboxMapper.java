package com.finanzen.api.utils.mapper;

import com.finanzen.api.adapters.out.outbox.JpaOutboxEntity;
import com.finanzen.api.domain.outbox.OutboxEvent;

public class OutboxMapper {
    public static OutboxEvent toDomain(JpaOutboxEntity entity) {
        if (entity == null) return null;

        return new OutboxEvent(
                entity.getId(),
                entity.getAggregateType(),
                entity.getAggregateId(),
                entity.getEventType(),
                entity.getPayload(),
                entity.getCreatedAt(),
                entity.isProcessed()
        );
    }


    public static JpaOutboxEntity toEntity(OutboxEvent domain) {
        if (domain == null) return null;

        return new JpaOutboxEntity(
                domain.getId(),
                domain.getAggregateType(),
                domain.getAggregateId(),
                domain.getEventType(),
                domain.getPayload(),
                domain.getCreatedAt(),
                domain.isProcessed()
        );
    }
}
