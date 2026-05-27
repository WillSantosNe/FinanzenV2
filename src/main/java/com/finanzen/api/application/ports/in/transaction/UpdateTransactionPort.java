package com.finanzen.api.application.ports.in.transaction;

import com.finanzen.api.domain.transaction.Transaction;

public interface UpdateTransactionPort {
    Transaction update(Long id, Transaction transaction);
}
