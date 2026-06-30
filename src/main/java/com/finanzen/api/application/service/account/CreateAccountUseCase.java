package com.finanzen.api.application.service.account;

import com.finanzen.api.application.exceptions.DuplicateAccountNumberException;
import com.finanzen.api.application.ports.in.account.CreateAccountPort;
import com.finanzen.api.application.ports.out.account.AccountRepositoryPort;
import com.finanzen.api.domain.account.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateAccountUseCase implements CreateAccountPort {

    private final AccountRepositoryPort repositoryPort;

    @Override
    public Account create(Account account, String userEmail) {
        if (repositoryPort.existsByAccountNumber(account.getAccountNumber())) {
            throw new DuplicateAccountNumberException("The account number is already in use.");
        }

        account.setUserEmail(userEmail);

        return repositoryPort.save(account);
    }
}
