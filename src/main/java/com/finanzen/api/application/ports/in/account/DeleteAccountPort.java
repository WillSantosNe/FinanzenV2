package com.finanzen.api.application.ports.in.account;

public interface DeleteAccountPort {
    /**
     * Executes the use case to logically or physically delete a account.
     * @param id the unique identifier of the account to be removed.
     * @param authenticatedEmail the user email.
     */
    void delete(Long id, String authenticatedEmail);
}
