package com.finanzen.api.adapters.in.account;

import com.finanzen.api.adapters.in.account.dto.AccountCreateDto;
import com.finanzen.api.adapters.in.account.dto.AccountGetDto;
import com.finanzen.api.application.ports.in.account.CreateAccountPort;
import com.finanzen.api.application.ports.in.account.FindAccountByIdPort;
import com.finanzen.api.domain.account.Account;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {

    private final CreateAccountPort createAccountPort;
    private final FindAccountByIdPort findAccountByIdPort;

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

}
