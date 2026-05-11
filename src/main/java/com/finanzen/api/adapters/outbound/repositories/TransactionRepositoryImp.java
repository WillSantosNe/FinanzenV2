package com.finanzen.api.adapters.outbound.repositories;

import java.util.Optional;

import com.finanzen.api.adapters.outbound.entities.JpaTransactionEntity;
import com.finanzen.api.domain.transaction.TransactionRepository;
import com.finanzen.api.utils.mapper.TransactionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.finanzen.api.domain.transaction.Transaction;

/**
 * Persistence Adapter (Outbound Adapter) for transactions.
 * <p>
 * This component acts as a bridge between the application's outbound port
 * ({@link TransactionRepository}) and the actual database infrastructure
 * (Spring Data JPA). It intercepts pure domain objects, translates them into
 * database entities using a mapper, performs the physical database operations,
 * and translates the results back to the domain model.
 * </p>
 */
@Component
public class TransactionRepositoryImp implements TransactionRepository {

    private final JpaTransactionRepository repository;

    public TransactionRepositoryImp(JpaTransactionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Transaction save(Transaction transaction) {
        // Converte dominio puro em entity
        JpaTransactionEntity entity = TransactionMapper.toEntity(transaction);

        // Salva no repository a entity criada
        JpaTransactionEntity savEntity = repository.save(entity);

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
