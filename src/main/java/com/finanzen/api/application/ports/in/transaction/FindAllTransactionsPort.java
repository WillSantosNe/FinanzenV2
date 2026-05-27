package com.finanzen.api.application.ports.in.transaction;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.domain.transaction.Transaction;

public interface FindAllTransactionsPort {
    PageResult<Transaction> findAllSystemWide(int page, int size);
    PageResult<Transaction> findAllByUserEmail(String userEmail, int page, int size);
}
