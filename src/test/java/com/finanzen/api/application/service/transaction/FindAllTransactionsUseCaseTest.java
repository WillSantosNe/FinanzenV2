package com.finanzen.api.application.service.transaction;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.exceptions.TransactionNotFoundException;
import com.finanzen.api.application.ports.out.transaction.TransactionRepositoryPort;
import com.finanzen.api.domain.transaction.Transaction;
import com.finanzen.api.domain.transaction.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Application: Find All Transaction Use Case Tests")
public class FindAllTransactionsUseCaseTest {

    @Mock
    private TransactionRepositoryPort repository;

    @InjectMocks
    private FindAllTransactionsUseCase useCase;

    @Test
    @DisplayName("Should return system-wide paginated transactions successfully")
    void shouldReturnSystemWideTransactionsSuccessfully() {
        // Arrange
        int page = 0;
        int size = 10;

        Transaction mockedTransaction = new Transaction(
                1L,
                "Lunch",
                new BigDecimal("30.00"),
                LocalDateTime.now(),
                TransactionType.EXPENSE,
                "user@gmail.com");

        PageResult<Transaction> mockPageResult = new PageResult<>(List.of(mockedTransaction), page, 1, 1);

        when(repository.findAllSystemWide(page, size)).thenReturn(mockPageResult);

        // Act
        PageResult<Transaction> result = useCase.findAllSystemWide(page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.data().size());
        assertEquals(0, result.currentPage());
        assertEquals(1, result.totalPages());
        assertEquals("Lunch", result.data().get(0).getDescription());

        verify(repository, times(1)).findAllSystemWide(page, size);

    }

    @Test
    @DisplayName("Should return filtered paginated transactions for a specific user email")
    void shouldReturnUserTransactionsSuccessfully() {
        // Arrange
        int page = 0;
        int size = 10;
        String userEmail = "willi@gmail.com";

        Transaction mockedTransaction = new Transaction(
                2L,
                "Salary",
                new BigDecimal("10000.00"),
                LocalDateTime.now(),
                TransactionType.INCOME,
                userEmail
        );

        PageResult<Transaction> mockPageResult = new PageResult<>(List.of(mockedTransaction), page, 1, 1);

        when(repository.findAllByUserEmail(userEmail, page, size)).thenReturn(mockPageResult);

        // Act
        PageResult<Transaction> result = useCase.findAllByUserEmail(userEmail, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.data().size());
        assertEquals(0, result.currentPage());
        assertEquals(1, result.totalPages());
        assertEquals("Salary", result.data().get(0).getDescription());

        verify(repository, times(1)).findAllByUserEmail(userEmail, page, size);
    }

    @Test
    @DisplayName("Should return empty page result when user has no transactions stored")
    void shouldReturnEmptyPageWhenUserHasNoTransactions(){
        // Arrange
        int page = 0;
        int size = 10;
        String userEmail = "willi@gmail.com";

        when(repository.findAllByUserEmail(userEmail, page, size)).thenReturn(
                new PageResult<>(List.of(), page, 0, 0)
        );

        // Act
        PageResult<Transaction> result = useCase.findAllByUserEmail(userEmail, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.data().size());
        assertEquals(0, result.currentPage());
        assertEquals(0, result.totalPages());
        verify(repository, times(1)).findAllByUserEmail(userEmail, page, size);
    }

    @Test
    @DisplayName("Should return empty page result when system wide search contains no records")
    void shouldReturnEmptyPageWhenSystemWideHasNoData(){
        // Arrange
        int page = 0;
        int size = 10;

        when(repository.findAllSystemWide(page, size)).thenReturn(
                new PageResult<>(List.of(), page, 0, 0)
        );

        // Act
        PageResult<Transaction> result = useCase.findAllSystemWide(page, size);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.data().size());
        assertEquals(0, result.currentPage());
        assertEquals(0, result.totalPages());
        verify(repository, times(1)).findAllSystemWide(page, size);
    }
}
