package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.in.transaction.FindAllTransactionsPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.Transaction;
import org.springframework.stereotype.Service;

/**
 * Application Service (Use Case) for listing transactions.
 * <p>
 * This service implements the {@link FindAllTransactionsPort}. It acts as an
 * orchestrator, delegating retrieval logic to the repository port while maintaining
 * a framework-agnostic response type ({@link PageResult}).
 * </p>
 */
@Service
public class FindAllTransactionsUseCase implements FindAllTransactionsPort {

    private final TransactionRepositoryPort repository;

    public FindAllTransactionsUseCase(TransactionRepositoryPort repository) {
        this.repository = repository;
    }

    /**
     * Executes the use case to retrieve a paginated list of all system-wide transactions.
     *
     * @param page the zero-based index of the page.
     * @param size the maximum number of records per page.
     * @return a {@link PageResult} containing the transactions.
     */
    @Override
    public PageResult<Transaction> findAllSystemWide(int page, int size) {
        return repository.findAllSystemWide(page, size);
    }

    /**
     * Executes the use case to retrieve a paginated list of transactions owned by a specific user.
     *
     * @param userEmail the email of the user owning the transactions.
     * @param page      the zero-based index of the page.
     * @param size      the maximum number of records per page.
     * @return a {@link PageResult} containing the user's transactions.
     */
    @Override
    public PageResult<Transaction> findAllByUserEmail(String userEmail, int page, int size) {
        return repository.findAllByUserEmail(userEmail, page, size);
    }
}