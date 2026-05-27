package com.finanzen.api.application.ports.in.transaction;

import com.finanzen.api.domain.transaction.Transaction;

public interface CreateTransactionPort {
    Transaction create(Transaction transaction, String userEmail);
}
