package com.finanzen.api.adapters.in.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finanzen.api.application.ports.in.account.UpdateAccountBalancePort;
import com.finanzen.api.domain.account.Account;
import com.finanzen.api.domain.transaction.Transaction;
import com.finanzen.api.domain.transaction.TransactionType;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@AllArgsConstructor
public class AccountKafkaConsumerAdapter {

    private final ObjectMapper objectMapper;
    private final UpdateAccountBalancePort updateAccountBalancePort;

    @KafkaListener(topics = "transaction-events", groupId = "finanzen-group")
    public void consumeTransactionCreated(String payload)
    {
        try {
            Transaction transaction = objectMapper.readValue(payload, Transaction.class);

            BigDecimal balanceDelta = transaction.getType() == TransactionType.EXPENSE
                ? transaction.getAmount().negate()
                : transaction.getAmount();

            Account account = updateAccountBalancePort.execute(transaction.getAccountId(), balanceDelta, transaction.getUserEmail());
            System.out.println("[UPDATE ACCOUNT BALANCE SERVICE] -> Account: " + account.getAccountNumber()
                    + " | Balance: " + account.getBalance());
        }
        catch (Exception e) {
            throw new RuntimeException("Error processing transaction event", e);
        }
    }
}
