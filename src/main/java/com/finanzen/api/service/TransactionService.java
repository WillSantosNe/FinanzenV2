package com.finanzen.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.finanzen.api.dto.TransactionCreateDto;
import com.finanzen.api.dto.TransactionGetDto;
import com.finanzen.api.model.Transaction;
import com.finanzen.api.repository.TransactionRepository;

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
}
