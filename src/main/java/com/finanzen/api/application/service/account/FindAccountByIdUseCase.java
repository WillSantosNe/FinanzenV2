package com.finanzen.api.application.service.account;

import com.finanzen.api.application.exceptions.AccountNotFoundException;
import com.finanzen.api.application.ports.in.account.FindAccountByIdPort;
import com.finanzen.api.application.ports.out.account.AccountRepositoryPort;
import com.finanzen.api.domain.account.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindAccountByIdUseCase implements FindAccountByIdPort {

    private final AccountRepositoryPort repositoryPort;

    /**
     * Executes the use case to retrieve a account by its unique identifier.
     *
     * @param id the unique identifier of the account.
     * @param authenticatedEmail the user email.
     * @return the requested {@link Account} domain object.
     * @throws AccountNotFoundException if no transaction is found in the repository.
     */
    @Override
    public Account findById(Long id, String authenticatedEmail) throws AccountNotFoundException {
        Account account =  repositoryPort.findById(id).orElseThrow(
                () -> new AccountNotFoundException("Account with id: " + id + " not found")
        );

        if (!account.getUserEmail().equals(authenticatedEmail)) {
            throw new AccountNotFoundException("Account with id: " + id + " not found");
        }

        return account;
    }
}
