package com.finanzen.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.finanzen.api.dto.TransactionCreateDto;
import com.finanzen.api.dto.TransactionGetDto;
import com.finanzen.api.dto.TransactionUpdateDto;
import com.finanzen.api.model.Transaction;
import com.finanzen.api.repository.TransactionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TransactionService {

    // Realizando Injeção de dependencias do repository
    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository){
        this.repository = repository;
    }

    public TransactionGetDto createTransaction(TransactionCreateDto dto){
        Transaction transaction = new Transaction();
        transaction.setDescription(dto.description());
        transaction.setAmount(dto.amount());
        transaction.setType(dto.type());
        transaction.setCreatedAt(LocalDateTime.now());

        repository.save(transaction);
        
        return new TransactionGetDto(transaction.getId(), transaction.getDescription(), transaction.getAmount(), transaction.getType(), transaction.getCreatedAt());
    }

    /**
     * Retrieves all transactions from the database.
     * <p>
     * This method fetches all transaction entities and maps them into {@link TransactionGetDto}
     * records to ensure that internal database models are not exposed to the client.
     * </p>
     *
     * @return a list of {@link TransactionGetDto} containing the transaction details.
     */
    public Page<TransactionGetDto> findAll(Pageable pagination) {
        return repository.findAll(pagination).map(t -> 
            new TransactionGetDto(t.getId(), t.getDescription(), t.getAmount(), t.getType(), t.getCreatedAt())
        );
    }

    /**
     * Retrieves a specific transaction by its unique identifier.
     * <p>
     * This method attempts to find a transaction in the database using the provided ID.
     * If found, it maps the entity to a {@link TransactionGetDto}. If not found, it throws
     * an {@link EntityNotFoundException}.
     * </p>
     *
     * @param id the unique identifier of the transaction to be retrieved.
     * @return a {@link TransactionGetDto} containing the transaction details.
     * @throws EntityNotFoundException if no transaction with the specified ID is found in the system.
     */
    public TransactionGetDto findById(Long id) throws EntityNotFoundException{
        Transaction transaction = repository.findById(id).orElseThrow(() -> 
            new EntityNotFoundException("Transaction with the id " + id + " not found in the system"));

        return new TransactionGetDto(transaction.getId(), transaction.getDescription(), transaction.getAmount(), transaction.getType(), transaction.getCreatedAt());
    }

    /**
     * Deletes a specific transaction from the database.
     * <p>
     * This method first verifies the existence of the transaction using the provided ID.
     * If the transaction is found, it is removed from the system. If it does not exist,
     * an {@link EntityNotFoundException} is thrown to prevent silent failures or generic database errors.
     * </p>
     *
     * @param id the unique identifier of the transaction to be deleted.
     * @throws EntityNotFoundException if no transaction with the specified ID exists.
     */
    public void delete(Long id){
        repository.findById(id).orElseThrow(() -> 
            new EntityNotFoundException("Transaction with the id " + id + " not found in the system"));

        repository.deleteById(id);
    }

    /**
     * Updates an existing transaction in the database.
     * <p>
     * This method verifies if a transaction exists for the given ID. If found, it updates
     * the allowed fields (description, amount, and type) using the provided {@link TransactionUpdateDto}.
     * The updated entity is then saved and mapped back to a {@link TransactionGetDto} directly from memory,
     * avoiding redundant database queries for the response.
     * </p>
     *
     * @param id the unique identifier of the transaction to be updated.
     * @param dto the data transfer object containing the new values for the transaction.
     * @return a {@link TransactionGetDto} containing the updated transaction details.
     * @throws EntityNotFoundException if no transaction with the specified ID is found.
     */
    public TransactionGetDto update(Long id, TransactionUpdateDto dto){
        Transaction transaction = repository.findById(id).orElseThrow(() -> 
            new EntityNotFoundException("Transaction with the id " + id + " not found in the system"));

        transaction.setDescription(dto.description());
        transaction.setAmount(dto.amount());
        transaction.setType(dto.type());

        repository.save(transaction);
        return new TransactionGetDto(transaction.getId(), transaction.getDescription(), transaction.getAmount(), transaction.getType(), transaction.getCreatedAt());
    }

}
