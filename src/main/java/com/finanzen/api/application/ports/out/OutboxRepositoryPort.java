package com.finanzen.api.application.ports.out;

import com.finanzen.api.domain.outbox.OutboxEvent;

public interface OutboxRepositoryPort {
    OutboxEvent save(OutboxEvent event);
}
