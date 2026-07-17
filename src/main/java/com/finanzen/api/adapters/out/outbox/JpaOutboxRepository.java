package com.finanzen.api.adapters.out.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaOutboxRepository extends JpaRepository<JpaOutboxEntity, Long> {
}
