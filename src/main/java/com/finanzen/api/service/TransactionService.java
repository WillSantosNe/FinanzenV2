package com.finanzen.api.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.finanzen.api.dto.TransactionCreateDto;
import com.finanzen.api.dto.TransactionGetDto;
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

    public Transaction createTransaction(TransactionCreateDto dto){
        Transaction transaction = new Transaction();
        transaction.setDescription(dto.description());
        transaction.setAmount(dto.amount());
        transaction.setType(dto.type());
        transaction.setCreatedAt(LocalDateTime.now());

        repository.save(transaction);
        return transaction;
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
    public List<TransactionGetDto> findAll(){
        return repository.findAll()
            .stream()
            .map(t -> new TransactionGetDto(
                t.getId(), t.getDescription(), t.getAmount(), t.getType(), t.getCreatedAt()))
            .toList();
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
}
