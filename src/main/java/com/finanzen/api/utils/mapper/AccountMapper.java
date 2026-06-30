package com.finanzen.api.utils.mapper;

import com.finanzen.api.adapters.out.account.JpaAccountEntity;
import com.finanzen.api.domain.account.Account;

public class AccountMapper {

    public static Account toDomain(JpaAccountEntity entity){
        if (entity == null) return null;

        return new Account(
                entity.getId(),
                entity.getAccountNumber(),
                entity.getBalance(),
                entity.getAccountType(),
                entity.getUserEmail()
        );
    }

    public static JpaAccountEntity toEntity(Account account){
        if (account == null) return null;

        return new JpaAccountEntity(
                account.getId(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getAccountType(),
                account.getUserEmail()
        );
    }
}
