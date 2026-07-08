package com.finanzen.api.adapters.out.account;

import com.finanzen.api.application.dto.common.PageResult;
import com.finanzen.api.application.ports.out.account.AccountRepositoryPort;
import com.finanzen.api.domain.account.Account;
import com.finanzen.api.utils.mapper.AccountMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class AccountRepository implements AccountRepositoryPort {

    private final JpaAccountRepository repository;

    @Override
    public Account save(Account account) {
        JpaAccountEntity entity = AccountMapper.toEntity(account);
        JpaAccountEntity savEntity  = repository.save(entity);
        return AccountMapper.toDomain(savEntity);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return repository.findById(id).map(AccountMapper::toDomain);
    }

    @Override
    public PageResult<Account> findAllSystemWide(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JpaAccountEntity> springPage = repository.findAll(pageable);

        List<Account> accounts = springPage.getContent().stream().map(AccountMapper::toDomain).toList();

        return new PageResult<>(accounts, springPage.getNumber(),  springPage.getTotalElements(), springPage.getTotalPages());
    }

    @Override
    public PageResult<Account> findAllByUserEmail(String userEmail, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<JpaAccountEntity> springPage = repository.findAllByUserEmail(userEmail, pageable);

        List<Account> accounts = springPage.getContent().stream().map(AccountMapper::toDomain).toList();

        return new PageResult<>(accounts, springPage.getNumber(),  springPage.getTotalElements(), springPage.getTotalPages());
    }

    //todo
    @Override
    public void deleteById(Long id) {

    }

    //todo
    @Override
    public Optional<Account> findByAccountNumber(String accountNumber) {
        return Optional.empty();
    }

    //todo
    @Override
    public boolean existsByAccountNumber(String accountNumber) {
        return repository.existsByAccountNumber(accountNumber);
    }
}
