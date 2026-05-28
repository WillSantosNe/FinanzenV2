package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.exceptions.TransactionNotFoundException;
import com.finanzen.api.application.ports.in.transaction.DeleteTransactionPort;
import com.finanzen.api.application.ports.in.transaction.FindTransactionByIdPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import org.springframework.stereotype.Service;

/**
 * Application Service (Use Case) for deleting existing transactions.
 * <p>
 * This service implements the {@link DeleteTransactionPort}. It ensures that the
 * deletion only proceeds if the transaction exists, by orchestrating a
 * "find-then-delete" flow using inbound ports.
 * </p>
 */
@Service
public class DeleteTransactionUseCase implements DeleteTransactionPort{

    private final TransactionRepositoryPort repository;
    private final FindTransactionByIdPort findTransactionByIdPort;

    public DeleteTransactionUseCase(TransactionRepositoryPort repository, FindTransactionByIdPort findTransactionByIdPort) {
        this.repository = repository;
        this.findTransactionByIdPort = findTransactionByIdPort;
    }

    /**
     * Executes the use case to delete a transaction.
     *
     * @param id the unique identifier of the transaction to be removed.
     * @throws TransactionNotFoundException if the ID does not exist.
     */
    @Override
    public void delete(Long id) {
        // A lógica de busca embutida no findById garante a validação antes da deleção
        findTransactionByIdPort.findById(id);
        repository.deleteById(id);
    }
}