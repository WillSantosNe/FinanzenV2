package com.finanzen.api.adapters.out.outbox;

import com.finanzen.api.domain.outbox.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaOutboxRepository extends JpaRepository<JpaOutboxEntity, Long> {
    List<JpaOutboxEntity> findByProcessedFalse();
}
