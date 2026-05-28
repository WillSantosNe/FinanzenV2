package com.finanzen.api.adapters.in.transaction;

import com.finanzen.api.adapters.in.transaction.dto.TransactionCreateDto;
import com.finanzen.api.adapters.in.transaction.dto.TransactionGetDto;
import com.finanzen.api.adapters.in.transaction.dto.TransactionUpdateDto;
import com.finanzen.api.application.dto.common.PageResult;
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

/**
 * REST controller acting as the inbound adapter for financial transactions.
 * <p>
 * This adapter mediates between the API client and the core transaction use cases.
 * It handles request mapping, input validation, and maps domain-level responses
 * back to web-friendly {@link TransactionGetDto} objects.
 * </p>
 */
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
     * Creates a new financial transaction for the authenticated user.
     *
     * @param dto         The transaction details (description, amount, type).
     * @param userDetails The authenticated user, automatically injected by Spring Security.
     * @return 201 Created with the persisted {@link TransactionGetDto}.
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

        Transaction transaction = createTransactionPort.create(transactionFromDto, userDetails.getUsername());

        TransactionGetDto transactionGetDto = new TransactionGetDto(
                transaction.getId(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreatedAt());

        return ResponseEntity.status(HttpStatus.CREATED).body(transactionGetDto);
    }

    /**
     * Retrieves a single transaction by its unique identifier.
     *
     * @param id The ID of the transaction to search for.
     * @return 200 OK with the transaction details.
     * @throws com.finanzen.api.application.exceptions.TransactionNotFoundException if the ID is not found.
     */
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
     * Admin-only endpoint to retrieve a paginated list of all system transactions.
     *
     * @param page Zero-based page index.
     * @param size Number of items per page.
     * @return 200 OK with a paginated result of all transactions.
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResult<TransactionGetDto>> findAllSystemWide(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResult<Transaction> domainPage = findAllTransactionsPort.findAllSystemWide(page, size);

        List<TransactionGetDto> dtos = domainPage.data().stream()
                .map(transaction -> new TransactionGetDto(
                        transaction.getId(),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getCreatedAt()
                )).toList();

        return ResponseEntity.ok(new PageResult<>(dtos, domainPage.currentPage(), domainPage.totalItems(), domainPage.totalPages()));
    }

    /**
     * Retrieves a paginated list of transactions belonging to the authenticated user.
     *
     * @param userDetails The authenticated user.
     * @param page        Zero-based page index.
     * @param size        Number of items per page.
     * @return 200 OK with the user's paginated transactions.
     */
    @GetMapping
    public ResponseEntity<PageResult<TransactionGetDto>> findAllMyTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResult<Transaction> domainPage = findAllTransactionsPort.findAllByUserEmail(
                userDetails.getUsername(), page, size);

        List<TransactionGetDto> dtos = domainPage.data().stream()
                .map(transaction -> new TransactionGetDto(
                        transaction.getId(),
                        transaction.getDescription(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getCreatedAt()
                )).toList();

        return ResponseEntity.ok(new PageResult<>(dtos, domainPage.currentPage(), domainPage.totalItems(), domainPage.totalPages()));
    }

    /**
     * Updates an existing transaction.
     *
     * @param id  The ID of the transaction to be updated.
     * @param dto The new transaction data.
     * @return 200 OK with the updated transaction details.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionGetDto> update(
            @PathVariable Long id,
            @RequestBody @Valid TransactionUpdateDto dto) {

        Transaction transactionFromDto = new Transaction(null, dto.description(), dto.amount(), null, dto.type(), null);
        Transaction transaction = updateTransactionPort.update(id, transactionFromDto);

        TransactionGetDto transactionGetDto = new TransactionGetDto(
                transaction.getId(), transaction.getDescription(), transaction.getAmount(),
                transaction.getType(), transaction.getCreatedAt());

        return ResponseEntity.ok(transactionGetDto);
    }

    /**
     * Permanently removes a transaction from the system.
     *
     * @param id The ID of the transaction to delete.
     * @return 204 No Content upon successful deletion.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteTransactionPort.delete(id);
        return ResponseEntity.noContent().build();
    }
}