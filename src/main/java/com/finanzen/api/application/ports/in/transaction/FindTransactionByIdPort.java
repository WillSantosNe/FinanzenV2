package com.finanzen.api.application.ports.in.transaction;

import com.finanzen.api.application.exceptions.TransactionNotFoundException;
import com.finanzen.api.domain.transaction.Transaction;

/**
 * Inbound Port (Driving Port) for retrieving a specific transaction.
 * <p>
 * This interface represents a primary entry point into the application's core.
 * It defines the use case contract for external adapters to interact with the
 * domain layer, ensuring that the outside world knows <b>what</b> the system
 * can do, without knowing <b>how</b> it is implemented.
 * </p>
 */
public interface FindTransactionByIdPort {

    /**
     * Executes the use case to find a transaction by its unique identifier.
     *
     * @param id the unique identifier of the transaction to retrieve.
     * @return the requested {@link Transaction} pure domain object.
     * @throws TransactionNotFoundException if the ID does not exist.
     */
    Transaction findById(Long id);
}