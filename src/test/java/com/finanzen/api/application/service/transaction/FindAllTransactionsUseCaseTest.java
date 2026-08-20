package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.Transaction;
import com.finanzen.api.domain.transaction.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(MockitoExtension.class)
public class FindAllTransactionsUseCaseTest {

    @InjectMocks
    private FindAllTransactionsUseCase findAllTransactionsUseCase;

    @Mock
    private TransactionRepositoryPort repository;

    @Test
    public void should_return_list_of_user_transactions_when_transactions_exist(){
        // Arrange
        String userEmail = "email@gmail.com";

        Transaction transaction1 = new Transaction(
                1L,
                "description",
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                TransactionType.EXPENSE,
                userEmail,
                1L);

        Transaction transaction2 = new Transaction(
                2L,
                "description",
                new BigDecimal("300.00"),
                LocalDateTime.now(),
                TransactionType.INCOME,
                userEmail,
                1L
        );

        List<Transaction> transactions = List.of(transaction1, transaction2);

        PageResult<Transaction> page = new PageResult<>(transactions, 0, 2, 1);
        BDDMockito.given(repository.findAllByUserEmail(any(), anyInt(), anyInt())).willReturn(page);

        // Act
        PageResult<Transaction> pageResult = findAllTransactionsUseCase.findAllByUserEmail(userEmail, 0,10);

        // Assert
        assertEquals(2, pageResult.data().size());
        assertEquals(2, pageResult.totalItems());
        assertEquals(1L, pageResult.data().get(0).getId());
    }

    @Test
    public void should_return_empty_list_when_database_is_empty(){
        // Assert
        List<Transaction> emptyList = new ArrayList<>();
        PageResult<Transaction> page = new PageResult<>(emptyList, 0, 0, 1);
        BDDMockito.given(repository.findAllSystemWide(anyInt(), anyInt())).willReturn(page);

        // Act
        PageResult<Transaction> pageResult = findAllTransactionsUseCase.findAllSystemWide(0,10);

        // Assert
        assertEquals(0, pageResult.data().size());
        assertEquals(0, pageResult.totalItems());
        assertEquals(Collections.emptyList(),  pageResult.data());
    }

}
