package com.finanzen.api.application.ports.in.account;

import com.finanzen.api.domain.account.Account;

public interface FindAccountByIdPort {
    /**
     * Executes the use case to find a account by its unique identifier.
     *
     * @param id the unique identifier of the account to retrieve.
     * @return the requested {@link Account} pure domain object.
     */
    Account findById(Long id);
}
