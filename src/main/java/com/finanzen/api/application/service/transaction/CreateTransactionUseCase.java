package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.ports.in.transaction.CreateTransactionPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.Transaction;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CreateTransactionUseCase implements CreateTransactionPort {

    private final TransactionRepositoryPort repository;

    public CreateTransactionUseCase(TransactionRepositoryPort repository) {
        this.repository = repository;
    }


    /**
     * Creates a new transaction in the system securely tied to an owner.
     *
     * @param transaction the transaction object containing the  details.
     * @param userEmail the email of the user who owns this transaction.
     * @return the created {@link Transaction} domain object.
     */
    @Override
    public Transaction create(Transaction transaction, String userEmail) {
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUserEmail(userEmail);
        return repository.save(transaction);
    }
}
