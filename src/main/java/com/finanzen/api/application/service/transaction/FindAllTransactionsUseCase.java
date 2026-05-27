package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.in.transaction.FindAllTransactionsPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.Transaction;
import org.springframework.stereotype.Service;

@Service
public class FindAllTransactionsUseCase implements FindAllTransactionsPort {

    private final TransactionRepositoryPort repository;

    public FindAllTransactionsUseCase(TransactionRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public PageResult<Transaction> findAllSystemWide(int page, int size) {
        return repository.findAllSystemWide(page, size);
    }

    @Override
    public PageResult<Transaction> findAllByUserEmail(String userEmail, int page, int size) {
        return repository.findAllByUserEmail(userEmail, page, size);
    }

}
