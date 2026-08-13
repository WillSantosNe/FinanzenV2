package com.finanzen.api.infrastructure.config.usecases;

import com.finanzen.api.application.ports.in.account.FindAccountByIdPort;
import com.finanzen.api.application.ports.in.transaction.FindTransactionByIdPort;
import com.finanzen.api.application.ports.out.transaction.TransactionEventPublisherPort;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.application.service.transaction.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TransactionUseCaseConfig {
    @Bean
    public CreateTransactionUseCase createTransactionUseCase(TransactionRepositoryPort repository,
                                                             TransactionEventPublisherPort transactionEventPublisherPort,
                                                             FindAccountByIdPort findAccountByIdPort) {
        return new CreateTransactionUseCase(repository, transactionEventPublisherPort, findAccountByIdPort);
    }

    @Bean
    public FindAllTransactionsUseCase findAllTransactionsUseCase(TransactionRepositoryPort repository) {
        return new FindAllTransactionsUseCase(repository);
    }

    @Bean
    public FindTransactionByIdUseCase findTransactionByIdUseCase(TransactionRepositoryPort repository) {
        return new FindTransactionByIdUseCase(repository);
    }

    @Bean
    public UpdateTransactionUseCase updateTransactionUseCase(TransactionRepositoryPort port,
                                                             FindTransactionByIdPort findTransactionByIdPort) {
        return new UpdateTransactionUseCase(port, findTransactionByIdPort);
    }

    @Bean
    public DeleteTransactionUseCase deleteTransactionUseCase(TransactionRepositoryPort port,
                                                             FindTransactionByIdPort findTransactionByIdPort) {
        return new DeleteTransactionUseCase(port, findTransactionByIdPort);
    }
}
