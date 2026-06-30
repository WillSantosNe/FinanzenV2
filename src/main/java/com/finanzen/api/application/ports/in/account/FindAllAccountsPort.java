package com.finanzen.api.application.ports.in.account;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.domain.account.Account;

public interface FindAllAccountsPort {

    /**
     * ADMIN ONLY: Retrieves a paginated list of all accounts across the entire system.
     *
     * @param page the zero-based index of the page to retrieve.
     * @param size the maximum number of items per page.
     * @return a pure {@link PageResult} containing the requested {@link Account} objects.
     */
    PageResult<Account> findAllSystemWide(int page, int size);

    /**
     * USER: Retrieves a paginated list of accounts belonging exclusively to a specific user.
     *
     * @param userEmail the email of the user acting as the owner filter.
     * @param page      the zero-based index of the page to retrieve.
     * @param size      the maximum number of items per page.
     * @return a pure {@link PageResult} containing the user's {@link Account} objects.
     */
    PageResult<Account> findAllByUserEmail(String userEmail, int page, int size);
}
