package com.finanzen.api.infrastructure.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
 * responsible for translating external web requests (JSON/DTOs) into domain 
 * commands by delegating the execution to the Inbound Port ({@link TransactionUseCase}). 
 * It enforces Role-Based Access Control (RBAC) and data isolation.
 * </p>
 */
@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionUseCase useCase;

    public TransactionController(TransactionUseCase useCase) {
        this.useCase = useCase;
    }

    /**
     * Handles the creation of a new transaction securely tied to the logged-in user.
     */
    @PostMapping 
    public ResponseEntity<TransactionGetDto> createTransaction(
            @RequestBody @Valid TransactionCreateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {
        
        // Passamos o e-mail extraído do Token direto para o UseCase
        Transaction transaction = useCase.create(dto, userDetails.getUsername());

        TransactionGetDto transactionGetDto = new TransactionGetDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionGetDto);
    }

    /**
     * ADMIN ONLY: Retrieves a paginated list of ALL transactions in the system.
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<TransactionGetDto>> findAllSystemWide(@PageableDefault(size = 10) Pageable pagination) {
        
        Page<Transaction> domainPage = useCase.findAllSystemWide(pagination);
        
        // Convertendo a Página de Domínio para Página de DTO
        Page<TransactionGetDto> dtoPage = domainPage.map(transaction -> new TransactionGetDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt()
        ));

        return ResponseEntity.ok(dtoPage);
    }

    /**
     * USER: Retrieves a paginated list of transactions belonging ONLY to the authenticated user.
     */
    @GetMapping
    public ResponseEntity<Page<TransactionGetDto>> findAllMyTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @PageableDefault(size = 10) Pageable pagination) {
        
        // Isolamento Horizontal: O usuário só busca pelo próprio e-mail
        Page<Transaction> domainPage = useCase.findAllByUserEmail(userDetails.getUsername(), pagination);
        
        Page<TransactionGetDto> dtoPage = domainPage.map(transaction -> new TransactionGetDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt()
        ));

        return ResponseEntity.ok(dtoPage);
    }

    /**
     * Retrieves a specific transaction by its ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionGetDto> findById(@PathVariable Long id) {
        Transaction transaction = useCase.findById(id);

        TransactionGetDto transactionGetDto = new TransactionGetDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt());

        return ResponseEntity.ok(transactionGetDto);
    }

    /**
     * Updates an existing transaction.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionGetDto> update(
            @PathVariable Long id,
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

    /**
     * Deletes a transaction from the system.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        useCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
