package com.finanzen.api.application.service.account;

import com.finanzen.api.application.ports.in.account.DeleteAccountPort;
import com.finanzen.api.application.ports.in.account.FindAccountByIdPort;
import com.finanzen.api.application.ports.out.account.AccountRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DeleteAccountUseCase implements DeleteAccountPort {

    private final AccountRepositoryPort repositoryPort;
    private final FindAccountByIdPort findAccountByIdPort;

    @Override
    public void delete(Long id, String authenticatedEmail) {
        findAccountByIdPort.findById(id, authenticatedEmail);
        repositoryPort.deleteById(id);
    }
}
