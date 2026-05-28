package com.finanzen.api.application.ports.in.transaction;

import com.finanzen.api.application.exceptions.TransactionNotFoundException;

/**
 * Inbound Port (Driving Port) for deleting a transaction.
 * <p>
 * This interface encapsulates the operation required to permanently remove
 * a transaction from the system. It guarantees that the core deletion logic
 * remains independent of the HTTP/Web layer (e.g., REST DELETE verbs).
 * </p>
 */
public interface DeleteTransactionPort {

    /**
     * Executes the use case to logically or physically delete a transaction.
     *
     * @param id the unique identifier of the transaction to be removed.
     * @throws TransactionNotFoundException if the transaction does not exist prior to deletion.
     */
    void delete(Long id);
}