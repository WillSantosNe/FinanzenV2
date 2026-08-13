package com.finanzen.api.infrastructure.config.usecases;

import com.finanzen.api.application.ports.in.account.FindAccountByIdPort;
import com.finanzen.api.application.ports.out.account.AccountRepositoryPort;
import com.finanzen.api.application.service.account.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AccountUseCaseConfig {
    @Bean
    public CreateAccountUseCase createAccountUseCase(AccountRepositoryPort repositoryPort){
        return new CreateAccountUseCase(repositoryPort);
    }

    @Bean
    public FindAllAccountsUseCase findAllAccountsUseCase(AccountRepositoryPort repositoryPort){
        return new FindAllAccountsUseCase(repositoryPort);
    }

    @Bean
    public FindAccountByIdUseCase findAccountByIdUseCase(AccountRepositoryPort repositoryPort){
        return new FindAccountByIdUseCase(repositoryPort);
    }

    @Bean
    public UpdateAccountBalanceUseCase updateAccountBalanceUseCase(AccountRepositoryPort repositoryPort,
                                                                   FindAccountByIdPort findAccountByIdPort){
        return new UpdateAccountBalanceUseCase(repositoryPort, findAccountByIdPort);
    }

    @Bean
    public DeleteAccountUseCase deleteAccountUseCase(AccountRepositoryPort repositoryPort,
                                                     FindAccountByIdPort findAccountByIdPort){
        return new DeleteAccountUseCase(repositoryPort, findAccountByIdPort);
    }
}
