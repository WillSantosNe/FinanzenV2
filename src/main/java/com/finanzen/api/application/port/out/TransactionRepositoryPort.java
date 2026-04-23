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
     * ADMIN ONLY: Retrieves a paginated list of all transactions from the storage.
     *
     * @param pagination the limit and sorting configurations.
     * @return a {@link Page} containing the mapped domain objects.
     */
    Page<Transaction> findAllSystemWide(Pageable pagination);

    /**
     * USER: Retrieves a paginated list of transactions filtered by the owner's email.
     *
     * @param userEmail the email of the user who owns the transactions.
     * @param pagination the limit and sorting configurations.
     * @return a {@link Page} containing the mapped domain objects.
     */
    Page<Transaction> findAllByUserEmail(String userEmail, Pageable pagination);

    /**
     * Physically removes a transaction from the storage.
     *
     * @param id the unique identifier of the transaction to be removed.
     */
    void deleteById(Long id);
}
