package com.finanzen.api.infrastructure.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.finanzen.api.application.port.out.TransactionRepositoryPort;
import com.finanzen.api.domain.Transaction;

/**
 * Persistence Adapter (Outbound Adapter) for transactions.
 * <p>
 * This component acts as a bridge between the application's outbound port
 * ({@link TransactionRepositoryPort}) and the actual database infrastructure
 * (Spring Data JPA). It intercepts pure domain objects, translates them into
 * database entities using a mapper, performs the physical database operations,
 * and translates the results back to the domain model.
 * </p>
 */
@Component
public class TransactionPersistenceAdapter implements TransactionRepositoryPort {

    private final TransactionRepository repository;

    public TransactionPersistenceAdapter(TransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        // Converte dominio puro em entity
        TransactionEntity entity = TransactionMapper.toEntity(transaction);

        // Salva no repository a entity criada
        TransactionEntity savEntity = repository.save(entity);

        // Retorna o dominio puro convertido de uma entity 
        return TransactionMapper.toDomain(savEntity);
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return repository.findById(id).map(TransactionMapper::toDomain);
    }

    @Override
    public Page<Transaction> findAllSystemWide(Pageable pagination) {
        // Busca absolutamente tudo no banco (Para Admins)
        return repository.findAll(pagination)
                .map(TransactionMapper::toDomain);
    }

    @Override
    public Page<Transaction> findAllByUserEmail(String userEmail, Pageable pagination) {
        // Manda o email para o JPA filtrar direto no SQL (Para Usuários Comuns)
        return repository.findAllByUserEmail(userEmail, pagination)
                .map(TransactionMapper::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
