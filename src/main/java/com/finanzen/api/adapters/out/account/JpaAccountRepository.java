package com.finanzen.api.adapters.out.account;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.domain.account.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaAccountRepository extends JpaRepository<JpaAccountEntity, Long> {

    Optional<JpaAccountEntity> findByAccountNumber(String accountNumber);

    Page<JpaAccountEntity> findAllByUserEmail(String userEmail, Pageable pageable);

    boolean existsByAccountNumber(String accountNumber);
}
