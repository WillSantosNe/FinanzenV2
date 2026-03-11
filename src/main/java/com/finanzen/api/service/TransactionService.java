package com.finanzen.api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.finanzen.api.dto.TransactionCreateDto;
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
}
