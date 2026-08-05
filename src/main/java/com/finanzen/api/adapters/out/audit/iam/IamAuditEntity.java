package com.finanzen.api.adapters.out.audit.iam;

import com.finanzen.api.domain.audit.IamAuditActionType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Document(collection = "iam_audit_logs")
public class IamAuditEntity {

    @Id
    private String id;

    @Field("user_id")
    private Long userId;

    @Field("user_email")
    private String userEmail;

    @Field("action_type")
    private IamAuditActionType actionType;

    private LocalDateTime timestamp;
}
