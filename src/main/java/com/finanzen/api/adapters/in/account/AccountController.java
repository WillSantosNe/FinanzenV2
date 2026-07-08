package com.finanzen.api.adapters.in.account;

import com.finanzen.api.adapters.in.account.dto.AccountCreateDto;
import com.finanzen.api.adapters.in.account.dto.AccountGetDto;
import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.in.account.CreateAccountPort;
import com.finanzen.api.application.ports.in.account.DeleteAccountPort;
import com.finanzen.api.application.ports.in.account.FindAccountByIdPort;
import com.finanzen.api.application.ports.in.account.FindAllAccountsPort;
import com.finanzen.api.domain.account.Account;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final CreateAccountPort createAccountPort;
    private final FindAccountByIdPort findAccountByIdPort;
    private final FindAllAccountsPort findAllAccountsPort;
    private final DeleteAccountPort deleteAccountPort;

    @PostMapping
    public ResponseEntity<AccountGetDto> create(
            @RequestBody @Valid AccountCreateDto dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Account accountFromDto = new Account(
                null,
                dto.accountNumber(),
                dto.balance(),
                dto.accountType(),
                null
        );

        Account account = createAccountPort.create(accountFromDto, userDetails.getUsername());

        AccountGetDto accountGetDto = new AccountGetDto(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getUserEmail()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(accountGetDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AccountGetDto> findById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Account account = findAccountByIdPort.findById(id, userDetails.getUsername());

        AccountGetDto accountGetDto = new AccountGetDto(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getUserEmail()
        );

        return ResponseEntity.ok(accountGetDto);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageResult<AccountGetDto>> findAllSystemWide(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResult<Account> domainPage = findAllAccountsPort.findAllSystemWide(page, size);

        List<AccountGetDto> dtos = domainPage
                .data()
                .stream()
                .map(
                account -> new AccountGetDto(
                        account.getId(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getAccountType(),
                        account.getUserEmail()
                ))
                .toList();

        return ResponseEntity.ok(new PageResult<>(
                        dtos, domainPage.currentPage(), domainPage.totalItems(), domainPage.totalPages()));
    }

    @GetMapping
    public ResponseEntity<PageResult<AccountGetDto>> findAllMyAccounts(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageResult<Account> domainPage = findAllAccountsPort.findAllByUserEmail(
                userDetails.getUsername(), page, size);

        List<AccountGetDto> dtos = domainPage.data().stream()
                .map(account -> new AccountGetDto(
                        account.getId(),
                        account.getAccountNumber(),
                        account.getBalance(),
                        account.getAccountType(),
                        account.getUserEmail()
                )).toList();

        return ResponseEntity.ok(new PageResult<>(dtos, domainPage.currentPage(), domainPage.totalItems(), domainPage.totalPages()));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        deleteAccountPort.delete(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
