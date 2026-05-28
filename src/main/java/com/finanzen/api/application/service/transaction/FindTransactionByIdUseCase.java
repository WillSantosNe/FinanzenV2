package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.exceptions.TransactionNotFoundException;
import com.finanzen.api.application.ports.in.transaction.FindTransactionByIdPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.Transaction;
import org.springframework.stereotype.Service;

/**
 * Application Service (Use Case) for retrieving a specific transaction.
 * <p>
 * This service implements the {@link FindTransactionByIdPort}. It encapsulates
 * the logic for fetching a single transaction from the persistent storage
 * and maps any potential "not found" scenario into a domain-specific exception.
 * </p>
 */
@Service
public class FindTransactionByIdUseCase implements FindTransactionByIdPort {

    private final TransactionRepositoryPort repository;

    public FindTransactionByIdUseCase(TransactionRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Executes the use case to retrieve a transaction by its unique identifier.
     *
     * @param id the unique identifier of the transaction.
     * @return the requested {@link Transaction} domain object.
     * @throws TransactionNotFoundException if no transaction is found in the repository.
     */
    @Override
    public Transaction findById(Long id) throws TransactionNotFoundException {
        return repository.findById(id).orElseThrow(
                () -> new TransactionNotFoundException("Transaction with the id " + id + " not found in the system"));
    }
}