package com.finanzen.api.adapters.out.outbox;

import com.finanzen.api.application.ports.out.OutboxRepositoryPort;
import com.finanzen.api.domain.outbox.OutboxEvent;
import com.finanzen.api.utils.mapper.OutboxMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OutboxRepository implements OutboxRepositoryPort {

    private final JpaOutboxRepository repository;

    @Override
    public OutboxEvent save(OutboxEvent event) {
        JpaOutboxEntity entity = OutboxMapper.toEntity(event);
        return OutboxMapper.toDomain(repository.save(entity));
    }
}
