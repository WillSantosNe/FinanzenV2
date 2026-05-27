package com.finanzen.api.adapters.out.transaction;

import java.util.List;
import java.util.Optional;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.utils.mapper.TransactionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.finanzen.api.domain.transaction.Transaction;

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
public class TransactionRepository implements TransactionRepositoryPort {

    private final JpaTransactionRepository repository;

    public TransactionRepository(JpaTransactionRepository repository) {
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
    public PageResult<Transaction> findAllSystemWide(int page, int size) {
        // Criando Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Chama o findAll nativo
        Page<JpaTransactionEntity> springPage = repository.findAll(pageable);

        // Converte as entidades para dominio
        List<Transaction> transactions = springPage.getContent().stream()
                .map(TransactionMapper::toDomain)
                .toList();

        // Empacota como PageResult
        return new PageResult<>(
                transactions,
                springPage.getNumber(),
                springPage.getTotalElements(),
                springPage.getTotalPages()
        );
    }


    @Override
    public PageResult<Transaction> findAllByUserEmail(String userEmail, int page, int size) {
        // Criando Pageable
        Pageable pageable = PageRequest.of(page, size);

        // Chama o findAllByUserEmail
        Page<JpaTransactionEntity> springPage = repository.findAllByUserEmail(userEmail, pageable);

        // Converte as entidades para dominio
        List<Transaction> transactions = springPage.getContent().stream()
                .map(TransactionMapper::toDomain)
                .toList();

        // Empacota como PageResult
        return new PageResult<>(
                transactions,
                springPage.getNumber(),
                springPage.getTotalElements(),
                springPage.getTotalPages()
        );
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
