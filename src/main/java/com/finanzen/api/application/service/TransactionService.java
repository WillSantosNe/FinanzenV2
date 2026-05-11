package com.finanzen.api.application.service;

import java.time.LocalDateTime;

import com.finanzen.api.application.usecases.TransactionUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finanzen.api.domain.transaction.TransactionRepository;
import com.finanzen.api.domain.transaction.Transaction;
import com.finanzen.api.application.dto.transaction.TransactionCreateDto;
import com.finanzen.api.application.dto.transaction.TransactionUpdateDto;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransactionService implements TransactionUseCase {

    // Agora utilizando a porta de persistencia OUT
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    /**
     * Creates a new transaction in the system securely tied to an owner.
     *
     * @param dto the data transfer object containing the transaction details.
     * @param userEmail the email of the user who owns this transaction.
     * @return the created {@link Transaction} domain object.
     */
    @Override
    public Transaction create(TransactionCreateDto dto, String userEmail) {
        // Transformando DTO de Entrada em Domain Pura
        Transaction transaction = new Transaction(
                null,
                dto.description(),
                dto.amount(),
                LocalDateTime.now(),
                dto.type(),
                userEmail
        );
        return repository.save(transaction);
    }

    /**
     * Retrieves a paginated list of all transactions (Admin usage).
     *
     * @param pagination the pagination and sorting information.
     * @return a {@link Page} of {@link Transaction} domain objects.
     */
    @Override
    public Page<Transaction> findAllSystemWide(Pageable pagination) {
        return repository.findAllSystemWide(pagination);
    }

    /**
     * Retrieves a paginated list of transactions filtered by the owner's email (User usage).
     *
     * @param userEmail the email of the owner.
     * @param pagination the pagination and sorting information.
     * @return a {@link Page} of {@link Transaction} domain objects.
     */
    @Override
    public Page<Transaction> findAllByUserEmail(String userEmail, Pageable pagination) {
        return repository.findAllByUserEmail(userEmail, pagination);
    }

    /**
     * Retrieves a specific transaction by its unique identifier.
     *
     * @param id the unique identifier of the transaction.
     * @return the {@link Transaction} domain object.
     * @throws EntityNotFoundException if no transaction is found.
     */
    @Override
    public Transaction findById(Long id) throws EntityNotFoundException {
        return repository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Transaction with the id " + id + " not found in the system"));
    }

    /**
     * Updates an existing transaction.
     *
     * @param id  the unique identifier of the transaction to be updated.
     * @param dto the data transfer object containing the new values.
     * @return the updated {@link Transaction} domain object.
     * @throws EntityNotFoundException if the transaction is not found.
     */
    @Override
    public Transaction update(Long id, TransactionUpdateDto dto) {
        Transaction transaction = this.findById(id); // Reutilizando nosso próprio método para lançar exceção!

        transaction.setDescription(dto.description());
        transaction.setAmount(dto.amount());
        transaction.setType(dto.type());

        repository.save(transaction);
        return transaction;
    }

    /**
     * Deletes a specific transaction from the system.
     *
     * @param id the unique identifier of the transaction to be deleted.
     * @throws EntityNotFoundException if the transaction does not exist prior to deletion.
     */
    @Override
    public void delete(Long id) {
        this.findById(id); // Verifica se existe antes de deletar
        repository.deleteById(id);
    }
}
