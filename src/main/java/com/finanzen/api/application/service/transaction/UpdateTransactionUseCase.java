package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.exceptions.TransactionNotFoundException;
import com.finanzen.api.application.ports.in.transaction.FindTransactionByIdPort;
import com.finanzen.api.application.ports.in.transaction.UpdateTransactionPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.Transaction;
import org.springframework.stereotype.Service;

/**
 * Application Service (Use Case) for updating transaction details.
 * <p>
 * This service implements the {@link UpdateTransactionPort}. It handles the
 * state transition of a transaction by fetching the record, applying new
 * values from the domain object, and persisting the change.
 * </p>
 */
@Service
public class UpdateTransactionUseCase implements UpdateTransactionPort {

    private final TransactionRepositoryPort repository;
    private final FindTransactionByIdPort findTransactionByIdPort;

    public UpdateTransactionUseCase(TransactionRepositoryPort repository, FindTransactionByIdPort findTransactionByIdPort) {
        this.repository = repository;
        this.findTransactionByIdPort = findTransactionByIdPort;
    }

    /**
     * Executes the update transaction use case.
     *
     * @param id          the unique identifier of the transaction.
     * @param transaction the domain object containing the new state.
     * @return the updated {@link Transaction} domain object.
     * @throws TransactionNotFoundException if the ID does not exist.
     */
    @Override
    public Transaction update(Long id, Transaction transaction, String authenticatedEmail) {
        Transaction transactionFind = findTransactionByIdPort.findById(id, authenticatedEmail);

        transactionFind.setDescription(transaction.getDescription());
        transactionFind.setAmount(transaction.getAmount());
        transactionFind.setType(transaction.getType());

        repository.save(transactionFind);

        return transactionFind;
    }
}