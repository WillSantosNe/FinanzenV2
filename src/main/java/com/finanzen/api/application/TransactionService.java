package com.finanzen.api.application;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.finanzen.api.application.port.in.TransactionUseCase;
import com.finanzen.api.application.port.out.TransactionRepositoryPort;
import com.finanzen.api.domain.Transaction;
import com.finanzen.api.infrastructure.dto.TransactionCreateDto;
import com.finanzen.api.infrastructure.dto.TransactionUpdateDto;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransactionService implements TransactionUseCase {

    // Agora utilizando a porta de persistencia OUT
    private final TransactionRepositoryPort persistencePort;

    public TransactionService(TransactionRepositoryPort persistencePort) {
        this.persistencePort = persistencePort;
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
        return persistencePort.save(transaction);
    }

    /**
     * Retrieves a paginated list of all transactions (Admin usage).
     *
     * @param pagination the pagination and sorting information.
     * @return a {@link Page} of {@link Transaction} domain objects.
     */
    @Override
    public Page<Transaction> findAllSystemWide(Pageable pagination) {
        return persistencePort.findAllSystemWide(pagination);
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
        // Nota: Você precisará adicionar este método na sua interface TransactionRepositoryPort
        return persistencePort.findAllByUserEmail(userEmail, pagination);
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
        return persistencePort.findById(id).orElseThrow(
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

        persistencePort.save(transaction);
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
        persistencePort.deleteById(id);
    }
}
