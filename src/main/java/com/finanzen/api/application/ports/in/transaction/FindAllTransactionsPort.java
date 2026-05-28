package com.finanzen.api.application.ports.in.transaction;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.domain.transaction.Transaction;

/**
 * Inbound Port (Driving Port) for retrieving paginated lists of transactions.
 * <p>
 * This interface provides the read-only contracts for listing transactions.
 * By returning a framework-agnostic {@link PageResult}, it actively prevents
 * persistence-specific pagination objects (like Spring Data's {@code Pageable})
 * from leaking into the core business logic.
 * </p>
 */
public interface FindAllTransactionsPort {

    /**
     * ADMIN ONLY: Retrieves a paginated list of all transactions across the entire system.
     *
     * @param page the zero-based index of the page to retrieve.
     * @param size the maximum number of items per page.
     * @return a pure {@link PageResult} containing the requested {@link Transaction} objects.
     */
    PageResult<Transaction> findAllSystemWide(int page, int size);

    /**
     * USER: Retrieves a paginated list of transactions belonging exclusively to a specific user.
     *
     * @param userEmail the email of the user acting as the owner filter.
     * @param page      the zero-based index of the page to retrieve.
     * @param size      the maximum number of items per page.
     * @return a pure {@link PageResult} containing the user's {@link Transaction} objects.
     */
    PageResult<Transaction> findAllByUserEmail(String userEmail, int page, int size);
}