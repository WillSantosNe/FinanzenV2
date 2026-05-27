package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.ports.in.transaction.FindTransactionByIdPort;
import com.finanzen.api.application.ports.in.transaction.UpdateTransactionPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.Transaction;
import org.springframework.stereotype.Service;

@Service
public class UpdateTransactionUseCase implements UpdateTransactionPort {

    private final TransactionRepositoryPort repository;
    private final FindTransactionByIdPort findTransactionByIdPort;

    public UpdateTransactionUseCase(TransactionRepositoryPort repository, FindTransactionByIdPort findTransactionByIdPort) {
        this.repository = repository;
        this.findTransactionByIdPort = findTransactionByIdPort;
    }

    @Override
    public Transaction update(Long id, Transaction transaction) {
        Transaction transactionFind = findTransactionByIdPort.findById(id);

        transactionFind.setDescription(transaction.getDescription());
        transactionFind.setAmount(transaction.getAmount());
        transactionFind.setType(transaction.getType());

        repository.save(transactionFind);

        return transactionFind;
    }
}
