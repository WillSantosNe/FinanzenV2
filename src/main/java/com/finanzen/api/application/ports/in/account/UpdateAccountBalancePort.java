package com.finanzen.api.application.ports.in.account;

import com.finanzen.api.domain.account.Account;
import java.math.BigDecimal;


public interface UpdateAccountBalancePort {

    /**
     * Orchestrates the use case to apply a financial delta to an account's balance.
     *
     * @param id                 the unique identifier of the target account.
     * @param amountDelta        the value to alter the balance (positive for credits, negative for debits).
     * @param authenticatedEmail the email of the principal triggering or authorizing the transaction.
     * @return the updated {@link Account} domain object post-persistence.
     */
    Account execute(Long id, BigDecimal amountDelta, String authenticatedEmail);
}