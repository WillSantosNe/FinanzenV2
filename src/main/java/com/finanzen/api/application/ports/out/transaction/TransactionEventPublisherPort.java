package com.finanzen.api.application.ports.out.transaction;

import com.finanzen.api.domain.transaction.Transaction;

/**
 * Outbound port for publishing transaction-related events to external message brokers.
 */
public interface TransactionEventPublisherPort {

    /**
     * Publishes an event indicating that a new transaction has been successfully created.
     *
     * @param transaction the domain entity containing the created transaction details.
     */
    void publishTransactionCreated(Transaction transaction);
}