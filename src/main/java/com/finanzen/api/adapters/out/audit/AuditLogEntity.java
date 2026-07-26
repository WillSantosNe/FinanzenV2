package com.finanzen.api.adapters.out.audit;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@Document(collection = "account_audit_logs")
public class AuditLogEntity {

    @Id
    private String id;

    @Field("account_id")
    private Long accountId;

    @Field("amount_changed")
    private BigDecimal amountChanged;

    @Field("user_email")
    private String userEmail;

    private LocalDateTime timestamp;
}
