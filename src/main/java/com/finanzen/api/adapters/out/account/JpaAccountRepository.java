package com.finanzen.api.adapters.out.account;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaAccountRepository extends JpaRepository<JpaAccountEntity, Long> {

    Optional<JpaAccountEntity> findByAccountNumber(String accountNumber);

    PageResult<JpaAccountEntity> findAllByUserEmail(String userEmail, int page, int size);

    boolean existsByAccountNumber(String accountNumber);
}
