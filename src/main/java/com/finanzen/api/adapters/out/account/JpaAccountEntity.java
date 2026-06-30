package com.finanzen.api.adapters.out.account;

import com.finanzen.api.domain.account.AccountType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity(name = "Account")
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JpaAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_number")
    private String  accountNumber;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(name = "user_email")
    private String userEmail;
}
