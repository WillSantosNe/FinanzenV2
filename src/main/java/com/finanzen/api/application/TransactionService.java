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
     * Creates a new transaction in the system.
     * <p>
     * Converts the inbound data transfer object into a pure Domain entity and
     * delegates the persistence to the outbound port.
     * </p>
     * * @param dto the data transfer object containing the transaction details.
     * 
     * @return the created {@link Transaction} domain object.
     */
    @Override
    public Transaction create(TransactionCreateDto dto) {
        // Transformando FTO de Entrada em Domain
        Transaction transaction = new Transaction(
                null,
                dto.description(),
                dto.amount(),
                LocalDateTime.now(),
                dto.type());
        return persistencePort.save(transaction);
    }

    /**
     * Retrieves a paginated list of all transactions.
     * <p>
     * This method fetches the transaction domain objects through the persistence
     * port,
     * abstracting away the underlying database technology.
     * </p>
     *
     * @param pagination the pagination and sorting information.
     * @return a {@link Page} of {@link Transaction} domain objects.
     */
    @Override
    public Page<Transaction> getAll(Pageable pagination) {
        return persistencePort.findAll(pagination);
    }

    /**
     * Retrieves a specific transaction by its unique identifier.
     * <p>
     * Fetches the domain object via the persistence port. If the record does not
     * exist,
     * it halts the flow to prevent further processing.
     * </p>
     *
     * @param id the unique identifier of the transaction.
     * @return the {@link Transaction} domain object.
     * @throws EntityNotFoundException if no transaction is found.
     */
    public Transaction getById(Long id) throws EntityNotFoundException {
        Transaction transaction = persistencePort.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Transaction with the id " + id + " not found in the system"));

        return transaction;
    }

    /**
     * Deletes a specific transaction from the system.
     * <p>
     * First verifies the existence of the domain object to ensure safe deletion.
     * If found, delegates the physical removal to the persistence port.
     * </p>
     *
     * @param id the unique identifier of the transaction to be deleted.
     * @throws EntityNotFoundException if the transaction does not exist prior to
     *                                 deletion.
     */
    public void delete(Long id) {
        persistencePort.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Transaction with the id " + id + " not found in the system"));

        persistencePort.deleteById(id);
    }

    /**
     * Updates an existing transaction.
     * <p>
     * Retrieves the existing domain object, mutates its state with the provided
     * inbound data,
     * and delegates the update operation to the persistence port.
     * </p>
     *
     * @param id  the unique identifier of the transaction to be updated.
     * @param dto the data transfer object containing the new values.
     * @return the updated {@link Transaction} domain object.
     * @throws EntityNotFoundException if the transaction is not found.
     */
    public Transaction update(Long id, TransactionUpdateDto dto) {
        Transaction transaction = persistencePort.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Transaction with the id " + id + " not found in the system"));

        transaction.setDescription(dto.description());
        transaction.setAmount(dto.amount());
        transaction.setType(dto.type());

        persistencePort.save(transaction);
        return transaction;
    }

}
