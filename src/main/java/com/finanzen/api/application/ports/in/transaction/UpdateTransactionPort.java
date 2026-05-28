package com.finanzen.api.application.ports.in.transaction;

import com.finanzen.api.application.exceptions.TransactionNotFoundException;
import com.finanzen.api.domain.transaction.Transaction;

/**
 * Inbound Port (Driving Port) for updating an existing transaction.
 * <p>
 * This interface defines the use case for mutating the state of a transaction.
 * It strictly separates the "intent" to update from the implementation details,
 * enforcing that business rules are evaluated before any persistence occurs.
 * </p>
 */
public interface UpdateTransactionPort {

    /**
     * Executes the use case to update a transaction's details.
     *
     * @param id          the unique identifier of the transaction to be updated.
     * @param transaction a domain object containing the new state (e.g., amount, description).
     * @return the fully updated {@link Transaction} domain object.
     * @throws TransactionNotFoundException if the ID does not exist.
     */
    Transaction update(Long id, Transaction transaction);
}