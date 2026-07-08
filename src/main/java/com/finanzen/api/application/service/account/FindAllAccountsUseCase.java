package com.finanzen.api.application.service.account;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.in.account.FindAllAccountsPort;
import com.finanzen.api.application.ports.out.account.AccountRepositoryPort;
import com.finanzen.api.domain.account.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FindAllAccountsUseCase implements FindAllAccountsPort {

    private final AccountRepositoryPort repositoryPort;

    /**
     * Executes the use case to retrieve a paginated list of all system-wide accounts.
     *
     * @param page the zero-based index of the page.
     * @param size the maximum number of records per page.
     * @return a {@link PageResult} containing the accounts.
     */
    @Override
    public PageResult<Account> findAllSystemWide(int page, int size) {
        return repositoryPort.findAllSystemWide(page, size);
    }

    /**
     * Executes the use case to retrieve a paginated list of accounts owned by a specific user.
     *
     * @param userEmail the email of the user owning the accounts.
     * @param page      the zero-based index of the page.
     * @param size      the maximum number of records per page.
     * @return a {@link PageResult} containing the user's accounts.
     */
    @Override
    public PageResult<Account> findAllByUserEmail(String userEmail, int page, int size) {
        return repositoryPort.findAllByUserEmail(userEmail, page, size);
    }
}
