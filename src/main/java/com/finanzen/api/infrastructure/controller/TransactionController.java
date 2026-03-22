package com.finanzen.api.infrastructure.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finanzen.api.application.port.in.TransactionUseCase;
import com.finanzen.api.domain.Transaction;
import com.finanzen.api.infrastructure.dto.TransactionCreateDto;
import com.finanzen.api.infrastructure.dto.TransactionGetDto;
import com.finanzen.api.infrastructure.dto.TransactionUpdateDto;

import jakarta.validation.Valid;

/**
 * Web Adapter (Inbound Adapter) for managing transactions.
 * <p>
 * This REST controller acts as the entry point for HTTP traffic. It is
 * responsible for
 * translating external web requests (JSON/DTOs) into domain commands by
 * delegating
 * the execution to the Inbound Port ({@link TransactionUseCase}). It also maps
 * the
 * domain responses back to safe DTOs, ensuring the domain model is never
 * exposed
 * directly to the client.
 * </p>
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    // Realizando injeção de dependencias do useCase (Nossa Porta de Entrada)
    private final TransactionUseCase useCase;

    public TransactionController(TransactionUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Handles the creation of a new transaction.
     *
     * @param dto the validated data transfer object containing the transaction
     *            details.
     * @return a {@link ResponseEntity} containing the created
     *         {@link TransactionGetDto} and a 201 Created status.
     */
    @PostMapping // Devemos usar @Valid para o Spring fazer as validações necessárias
    public ResponseEntity<TransactionGetDto> createTransaction(@RequestBody @Valid TransactionCreateDto dto) {
        Transaction transaction = useCase.create(dto);

        TransactionGetDto transactionGetDto = new TransactionGetDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionGetDto);
    }

    /**
     * Retrieves a paginated list of transactions.
     *
     * @param pagination the default or client-provided pagination and sorting
     *                   parameters.
     * @return a {@link ResponseEntity} containing a paginated list of
     *         {@link TransactionGetDto}.
     */
    @GetMapping
    public ResponseEntity<Page<TransactionGetDto>> findAll(
            @PageableDefault(size = 10, sort = { "createdAt" }) Pageable pagination) {

        Page<Transaction> transactionsPage = useCase.getAll(pagination);

        Page<TransactionGetDto> dtosPage = transactionsPage.map(transaction -> new TransactionGetDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt()));

        return ResponseEntity.ok(dtosPage);
    }

    /**
     * Retrieves a specific transaction by its ID.
     *
     * @param id the unique identifier of the transaction.
     * @return a {@link ResponseEntity} containing the {@link TransactionGetDto}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionGetDto> findById(@PathVariable Long id) {
        Transaction transaction = useCase.getById(id);

        TransactionGetDto transactionGetDto = new TransactionGetDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt());

        return ResponseEntity.ok(transactionGetDto);
    }

    /**
     * Deletes a transaction from the system.
     *
     * @param id the unique identifier of the transaction to be deleted.
     * @return a {@link ResponseEntity} with a 204 No Content status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        useCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Updates an existing transaction.
     *
     * @param id  the unique identifier of the transaction to be updated.
     * @param dto the validated data transfer object containing the new values.
     * @return a {@link ResponseEntity} containing the updated
     *         {@link TransactionGetDto}.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionGetDto> update(@PathVariable Long id,
            @RequestBody @Valid TransactionUpdateDto dto) {

        Transaction transaction = useCase.update(id, dto);

        TransactionGetDto transactionGetDto = new TransactionGetDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt());

        return ResponseEntity.ok(transactionGetDto);
    }
}
