package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.ports.in.transaction.DeleteTransactionPort;
import com.finanzen.api.application.ports.in.transaction.FindTransactionByIdPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class DeleteTransactionUseCase implements DeleteTransactionPort{

    private final TransactionRepositoryPort repository;
    private final FindTransactionByIdPort findTransactionByIdPort;

    public DeleteTransactionUseCase(TransactionRepositoryPort repository, FindTransactionByIdPort findTransactionByIdPort) {
        this.repository = repository;
        this.findTransactionByIdPort = findTransactionByIdPort;
    }

    @Override
    public void delete(Long id) {
        findTransactionByIdPort.findById(id); // Ja vai dar o erro se nao houver o id
        repository.deleteById(id);
    }
}
