package com.finanzen.api.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.finanzen.api.domain.Transaction;
import com.finanzen.api.infrastructure.dto.TransactionCreateDto;
import com.finanzen.api.infrastructure.dto.TransactionUpdateDto;

/**
 * Inbound Port for Transaction use cases.
 * <p>
 * This interface dictates the business rules offered by the application. 
 * It is consumed by inbound adapters (such as REST Controllers) and 
 * isolates the core system from user interface technologies.
 * </p>
 */
public interface TransactionUseCase {
    
    /**
     * Processes the creation of a new financial transaction and links it to a user.
     *
     * @param dto the data transfer object containing the creation details.
     * @param userEmail the email of the authenticated user creating the transaction.
     * @return the saved pure {@link Transaction} domain object.
     */
    Transaction create(TransactionCreateDto dto, String userEmail);
    
    /**
     * Retrieves a specific transaction by its unique identifier.
     *
     * @param id the transaction identifier.
     * @return the corresponding {@link Transaction} domain object.
     */
    Transaction findById(Long id);
    
    /**
     * ADMIN ONLY: Retrieves a paginated list of all registered transactions in the system.
     *
     * @param pagination the pagination and sorting information.
     * @return a {@link Page} containing {@link Transaction} domain objects.
     */
    Page<Transaction> findAllSystemWide(Pageable pagination);

    /**
     * USER: Retrieves a paginated list of transactions belonging to a specific user.
     *
     * @param userEmail the email of the owner.
     * @param pagination the pagination and sorting information.
     * @return a {@link Page} containing {@link Transaction} domain objects.
     */
    Page<Transaction> findAllByUserEmail(String userEmail, Pageable pagination);
    
    /**
     * Processes the full update of an existing transaction.
     *
     * @param id the identifier of the transaction to be updated.
     * @param dto the data transfer object containing the new values.
     * @return the updated {@link Transaction} domain object.
     */
    Transaction update(Long id, TransactionUpdateDto dto);
    
    /**
     * Permanently removes a transaction from the system.
     *
     * @param id the identifier of the transaction to be deleted.
     */
    void delete(Long id);
}
