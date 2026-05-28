package com.finanzen.api.application.ports.in.transaction;

import com.finanzen.api.domain.transaction.Transaction;

/**
 * Inbound Port (Driving Port) for creating a new financial transaction.
 * <p>
 * This interface defines the use case contract for registering a transaction.
 * It ensures that external adapters (like REST controllers) interact with the
 * core domain using pure business objects, keeping the application layer
 * completely unaware of web-specific delivery mechanisms or DTOs.
 * </p>
 */
public interface CreateTransactionPort {

    /**
     * Executes the use case to create a new transaction tied to a specific user.
     *
     * @param transaction the pure domain object containing the initial transaction details.
     * @param userEmail   the email of the authenticated user who owns this transaction.
     * @return the persisted {@link Transaction} domain object, populated with its generated ID.
     */
    Transaction create(Transaction transaction, String userEmail);
}