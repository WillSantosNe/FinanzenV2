package com.finanzen.api.application.ports.out.transaction;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.domain.transaction.Transaction;

import java.util.Optional;

/**
 * Outbound Port (Driven Port) for Transaction persistence.
 * <p>
 * This interface defines the contract that the core application layer requires to
 * store and retrieve data. It exclusively handles pure domain objects
 * ({@link Transaction}) and neutral DTOs ({@link PageResult}), ensuring that
 * the business logic remains 100% agnostic to the underlying database (SQL/NoSQL)
 * or persistence frameworks (like JPA or Hibernate).
 * </p>
 */
public interface TransactionRepositoryPort {

    /**
     * Persists a new transaction or updates an existing one in the underlying storage.
     *
     * @param transaction the pure domain entity to be persisted.
     * @return the {@link Transaction} with its persisted state (e.g., generated ID).
     */
    Transaction save(Transaction transaction);

    /**
     * Retrieves a transaction by its unique identifier.
     *
     * @param id the unique identifier of the transaction.
     * @return an {@link Optional} containing the transaction if found, or empty otherwise.
     */
    Optional<Transaction> findById(Long id);

    /**
     * ADMIN ONLY: Retrieves a paginated list of all transactions from the storage.
     * <p>
     * Used for system-wide auditing and administrative oversight.
     * </p>
     *
     * @param page the zero-based index of the page to retrieve.
     * @param size the maximum number of items per page.
     * @return a pure {@link PageResult} containing the requested {@link Transaction} objects.
     */
    PageResult<Transaction> findAllSystemWide(int page, int size);

    /**
     * USER: Retrieves a paginated list of transactions filtered by the owner's email.
     *
     * @param userEmail the email of the authenticated user.
     * @param page      the zero-based index of the page to retrieve.
     * @param size      the maximum number of items per page.
     * @return a pure {@link PageResult} containing the user's {@link Transaction} objects.
     */
    PageResult<Transaction> findAllByUserEmail(String userEmail, int page, int size);

    /**
     * Physically removes a transaction from the storage.
     *
     * @param id the unique identifier of the transaction to be removed.
     */
    void deleteById(Long id);


    boolean existsDuplicateRecentTransaction(
            Long accountId,
            java.math.BigDecimal amount,
            String description,
            com.finanzen.api.domain.transaction.TransactionType type,
            java.time.LocalDateTime limitTime
    );
}