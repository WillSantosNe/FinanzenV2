package com.finanzen.api.application.port.out;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finanzen.api.domain.Transaction;

/**
 * Outbound Port for Transaction persistence.
 * <p>
 * This interface defines the contract that the application layer requires to 
 * store and retrieve data. It exclusively handles pure domain objects 
 * ({@link Transaction}), ensuring that the business logic remains agnostic 
 * to the underlying database or persistence framework.
 * </p>
 */
public interface TransactionRepositoryPort {

    // Usando o domínio, e não a entity do banco
    /**
     * Saves or updates a domain object in the underlying storage.
     *
     * @param transaction the domain entity to be persisted.
     * @return the {@link Transaction} with its persisted state (e.g., generated ID).
     */
    Transaction save(Transaction transaction);

    /**
     * Retrieves a domain object by its unique identifier.
     *
     * @param id the unique identifier of the transaction.
     * @return an {@link Optional} containing the transaction if found, or empty otherwise.
     */
    Optional<Transaction> findById(Long id);

    /**
     * Retrieves a paginated list of transactions from the storage.
     *
     * @param pagination the limit and sorting configurations.
     * @return a {@link Page} containing the mapped domain objects.
     */
    Page<Transaction> findAll(Pageable pagination);

    /**
     * Physically removes a transaction from the storage.
     *
     * @param id the unique identifier of the transaction to be removed.
     */
    void deleteById(Long id);
}
