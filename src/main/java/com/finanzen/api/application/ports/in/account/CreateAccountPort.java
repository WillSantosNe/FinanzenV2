package com.finanzen.api.application.ports.in.account;

import com.finanzen.api.domain.account.Account;

public interface CreateAccountPort {
    /**
     * Executes the use case to create a new account tied to a specific user.
     *
     * @param account the pure domain object containing the initial account details.
     * @param userEmail   the email of the authenticated user who owns this account.
     * @return the persisted {@link Account} domain object, populated with its generated ID.
     */
    Account create(Account account, String userEmail);
}
