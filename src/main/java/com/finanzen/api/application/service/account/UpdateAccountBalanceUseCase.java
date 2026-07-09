package com.finanzen.api.application.service.account;

import com.finanzen.api.application.ports.in.account.FindAccountByIdPort;
import com.finanzen.api.application.ports.in.account.UpdateAccountBalancePort;
import com.finanzen.api.application.ports.out.account.AccountRepositoryPort;
import com.finanzen.api.domain.account.Account;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Application Service orchestrating the balance update operations.
 * <p>
 * This class implements the {@link UpdateAccountBalancePort}, coordinates the retrieval
 * of the target domain entity, delegates the arithmetic and safety verifications to the
 * domain layer, and ensures the new system state is properly persisted via the outbound port.
 * </p>
 */
@Service
@AllArgsConstructor
public class UpdateAccountBalanceUseCase implements UpdateAccountBalancePort {

    private final AccountRepositoryPort repositoryPort;
    private final FindAccountByIdPort findAccountByIdPort;

    @Override
    public Account execute(Long id, BigDecimal amountDelta, String authenticatedEmail) {
        Account account = findAccountByIdPort.findById(id, authenticatedEmail);

        account.applyDelta(amountDelta);

        return repositoryPort.save(account);
    }
}