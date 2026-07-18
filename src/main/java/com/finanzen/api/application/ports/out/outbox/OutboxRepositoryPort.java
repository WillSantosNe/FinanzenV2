package com.finanzen.api.application.ports.out.outbox;

import com.finanzen.api.adapters.out.outbox.JpaOutboxEntity;
import com.finanzen.api.domain.outbox.OutboxEvent;

import java.util.List;

public interface OutboxRepositoryPort {
    OutboxEvent save(OutboxEvent event);
    List<OutboxEvent> findUnprocessedEvents();
}
