package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.ports.in.transaction.CreateTransactionPort;
import com.finanzen.api.application.ports.out.transaction.TransactionEventPublisherPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Application Service (Use Case) for creating new transactions.
 * <p>
 * This service implements the {@link CreateTransactionPort} to handle the
 * business logic of creating a new financial record. It enriches the domain
 * object with system-generated data (e.g., current timestamp, owner ownership)
 * before persistence.
 * </p>
 */
@Service
@AllArgsConstructor
public class CreateTransactionUseCase implements CreateTransactionPort {

    private final TransactionRepositoryPort repository;
    private final TransactionEventPublisherPort eventPublisher;

    /**
     * Executes the use case to create a new transaction.
     *
     * @param transaction the domain object containing initial transaction details.
     * @param userEmail   the email of the authenticated user who owns this transaction.
     * @return the created {@link Transaction} domain object, including the generated ID and timestamp.
     */
    @Override
    @Transactional // Integridade, se o banco falhar nao publica no kafka
    public Transaction create(Transaction transaction, String userEmail) {
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUserEmail(userEmail);
        Transaction savedTransaction = repository.save(transaction);

        // Disparando evento assincrono para o Kafka com o Transaction ID
        eventPublisher.publishTransactionCreated(savedTransaction);

        return savedTransaction;
    }
}