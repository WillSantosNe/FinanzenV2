package com.finanzen.api.adapters.in.transaction;

import com.finanzen.api.adapters.in.transaction.dto.TransactionCreateDto;
import com.finanzen.api.adapters.in.transaction.dto.TransactionGetDto;
import com.finanzen.api.adapters.in.transaction.dto.TransactionUpdateDto;
import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.in.transaction.*;
import com.finanzen.api.application.ports.in.transaction.*;
import com.finanzen.api.domain.transaction.Transaction;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final CreateTransactionPort createTransactionPort;
    private final FindTransactionByIdPort findTransactionByIdPort;
    private final FindAllTransactionsPort findAllTransactionsPort;
    private final UpdateTransactionPort updateTransactionPort;
    private final DeleteTransactionPort deleteTransactionPort;

    public TransactionController(CreateTransactionPort createTransactionPort, FindTransactionByIdPort findTransactionByIdPort, FindAllTransactionsPort findAllTransactionsPort, UpdateTransactionPort updateTransactionPort, DeleteTransactionPort deleteTransactionPort) {
        this.createTransactionPort = createTransactionPort;
        this.findTransactionByIdPort = findTransactionByIdPort;
        this.findAllTransactionsPort = findAllTransactionsPort;
        this.updateTransactionPort = updateTransactionPort;
        this.deleteTransactionPort = deleteTransactionPort;
    }

    /**
     * Handles the creation of a new transaction securely tied to the logged-in user.
     */
    @PostMapping
    public ResponseEntity<TransactionGetDto> create(
            @RequestBody @Valid TransactionCreateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Transaction transactionFromDto = new Transaction(
                null,
                dto.description(),
                dto.amount(),
                null,
                dto.type(),
                null);

        // Passando o e-mail extraído do Token direto para o UseCase
        Transaction transaction = createTransactionPort.create(transactionFromDto, userDetails.getUsername());

        // Transaction vinda do banco de dados
        TransactionGetDto transactionGetDto = new TransactionGetDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionGetDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionGetDto> findById(@PathVariable Long id) {
        Transaction transaction = findTransactionByIdPort.findById(id);

        TransactionGetDto transactionGetDto = new TransactionGetDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt());

        return ResponseEntity.ok(transactionGetDto);
    }

    /**
     * ADMIN ONLY: Retrieves a paginated list of ALL transactions in the system.
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResult<TransactionGetDto>> findAllSystemWide(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResult<Transaction> domainPage = findAllTransactionsPort.findAllSystemWide(page, size);

        // Traduzindo lista do domínio para dto
        List<TransactionGetDto> dtos = domainPage.data().stream()
                .map(transaction -> new TransactionGetDto(
                        transaction.getId(),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getCreatedAt()
                )).toList();

        // Montando o PageResult
        PageResult<TransactionGetDto> dtoPage = new PageResult<>(
                dtos,
                domainPage.currentPage(),
                domainPage.totalItems(),
                domainPage.totalPages()
        );

        return ResponseEntity.ok(dtoPage);
    }

    /**
     * USER: Retrieves a paginated list of transactions belonging ONLY to the authenticated user.
     */
    @GetMapping
    public ResponseEntity<PageResult<TransactionGetDto>> findAllMyTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResult<Transaction> domainPage = findAllTransactionsPort.findAllByUserEmail(
                userDetails.getUsername(),
                page,
                size
        );

        // Traduzindo lista do domínio para dto
        List<TransactionGetDto> dtos = domainPage.data().stream()
                .map(transaction -> new TransactionGetDto(
                        transaction.getId(),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getCreatedAt()
                )).toList();

        // Montando o PageResult
        PageResult<TransactionGetDto> dtoPage = new PageResult<>(
                dtos,
                domainPage.currentPage(),
                domainPage.totalItems(),
                domainPage.totalPages()
        );

        return ResponseEntity.ok(dtoPage);
    }


    /**
     * Updates an existing transaction.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionGetDto> update(
            @PathVariable Long id,
            @RequestBody @Valid TransactionUpdateDto dto) {

        Transaction transactionFromDto = new Transaction(
                null,
                dto.description(),
                dto.amount(),
                null,
                dto.type(),
                null);

        Transaction transaction = updateTransactionPort.update(id, transactionFromDto);

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
        deleteTransactionPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}
