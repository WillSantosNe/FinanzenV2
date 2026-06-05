package com.finanzen.api.adapters.in.transaction;

import com.finanzen.api.BaseIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * End-to-end integration tests for the Transaction API endpoints.
 * Reuses the live PostgreSQL Docker container infrastructure.
 */
@Transactional
@AutoConfigureMockMvc // Instancia Mock para emular um HTTP
@DisplayName("Integration: Transaction Controller E2E Tests")
public class TransactionControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc; // Emulando uma requisicao HTTP

    @Test
    @DisplayName("Should create a transaction via HTTP API and persist it in PostgreSQL")
    void shouldCreateTransactionViaApiSuccessfully() throws Exception {
        // Arrange
        String transactionJson = """
                {
                    "description": "Cadeira Ergonomica",
                    "amount": 1200.50,
                    "type": "EXPENSE"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/transactions")
                        .with(user("dev@finanzen.com").roles("USER")) // SpringSecutiry injeta o usuario mockado como USER
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(transactionJson)) // Conteúdo JSON
                .andExpect(status().isCreated()) // Valida código HTTP 201
                .andExpect(jsonPath("$.id").exists()) // Valida que o database gerou a chame primaria
                .andExpect(jsonPath("$.description").value("Cadeira Ergonomica"))
                .andExpect(jsonPath("$.amount").value(1200.50));
    }


    @Test
    @DisplayName("Should return 404 Not Found when trying to delete a non-existing transaction")
    void shouldReturnNotFoundWhenTransactionToIdDoesNotExist() throws Exception {
        // Arrange
        Long id = 1L;

        // Act & Assert
        mockMvc.perform(delete("/transactions/" + id)
                        .with(user("dev@finanzen.com").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Valida o HTTP 404
    }


    @Test
    @DisplayName("Should return 400 Bad Request when transaction amount is negative")
    void shouldReturnBadRequestWhenAmountIsNegative() throws Exception {
        // Arrange
        String transactionJson = """
                {
                    "description": "Cadeira Ergonomica",
                    "amount": -1200.50,
                    "type": "EXPENSE"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/transactions")
                .with(user("dev@finanzen.com").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(transactionJson))
                .andExpect(status().isBadRequest()); // Valida o HTTP 400
    }
}
