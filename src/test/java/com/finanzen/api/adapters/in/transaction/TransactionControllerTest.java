package com.finanzen.api.adapters.in.transaction;

import com.finanzen.api.adapters.in.transaction.dto.TransactionCreateDto;
import com.finanzen.api.application.ports.in.transaction.*;
import com.finanzen.api.domain.transaction.Transaction;
import com.finanzen.api.domain.transaction.TransactionType;
import com.finanzen.api.infrastructure.config.CustomUserDetailsService;
import com.finanzen.api.infrastructure.config.SecurityConfig;
import com.finanzen.api.infrastructure.config.TokenService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TransactionController.class)
@Import(SecurityConfig.class) // Traz as regras de segurança reais
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean private CreateTransactionPort createTransactionPort;
    @MockitoBean private FindTransactionByIdPort findTransactionByIdPort;
    @MockitoBean private FindAllTransactionsPort findAllTransactionsPort;
    @MockitoBean private UpdateTransactionPort updateTransactionPort;
    @MockitoBean private DeleteTransactionPort deleteTransactionPort;

    @MockitoBean private TokenService tokenService;
    @MockitoBean private CustomUserDetailsService userDetailsService;

    @Test
    public void should_return_403_unauthorized_when_no_token_provided() throws Exception {
        // Arrange
        TransactionCreateDto dto = new TransactionCreateDto(
                "description",
                new BigDecimal("100.00"),
                TransactionType.INCOME,
                1L);

        String payloadJson =  objectMapper.writeValueAsString(dto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                .post("/transactions")
                .content(payloadJson)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "email@gmail.com", roles = {"USER"})
    public void should_return_400_bad_request_when_transaction_payload_is_invalid() throws Exception {
        // Arrange
        TransactionCreateDto dto = new TransactionCreateDto(
                "description",
                new BigDecimal("-100.00"),
                TransactionType.INCOME,
                1L);

        String payloadJson =  objectMapper.writeValueAsString(dto);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(payloadJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "email@gmail.com", roles = {"USER"})
    public void should_return_201_created_when_request_is_valid() throws Exception {

        // Arrange
        TransactionCreateDto dto = new TransactionCreateDto(
                "description",
                new BigDecimal("100.00"),
                TransactionType.INCOME,
                1L);

        String payloadJson =  objectMapper.writeValueAsString(dto);

        Transaction transactionCreated = new Transaction(
                1L,
                "description",
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                TransactionType.INCOME,
                "email@gmail.com",
                1L
        );

        BDDMockito.given(createTransactionPort.create(any(Transaction.class), any())).willReturn(transactionCreated);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/transactions")
                        .content(payloadJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.amount").value(new BigDecimal("100.0")));
    }
}