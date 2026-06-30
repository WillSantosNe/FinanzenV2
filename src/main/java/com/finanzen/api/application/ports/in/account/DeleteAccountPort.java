package com.finanzen.api.application.ports.in.account;

public interface DeleteAccountPort {
    /**
     * Executes the use case to logically or physically delete a account.
     *
     * @param id the unique identifier of the account to be removed.
     */
    void delete(Long id);
}
