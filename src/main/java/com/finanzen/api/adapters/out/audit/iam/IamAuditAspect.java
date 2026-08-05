package com.finanzen.api.adapters.out.audit.iam;

import com.finanzen.api.domain.audit.IamAuditActionType;
import com.finanzen.api.domain.user.User;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Aspect responsible for intercepting Identity and Access Management (IAM) operations.
 * It silently records an immutable audit trail in the NoSQL database (MongoDB)
 * whenever a user lifecycle event occurs, such as account creation.
 */
@Aspect
@Component
@AllArgsConstructor
public class IamAuditAspect {

    private final MongoIamAuditRepository repository;

    /**
     * Intercepts the execution of the user creation use case upon successful return.
     * Extracts the persisted user data to store an audit log.
     *
     * @param jp     The join point containing method execution context.
     * @param result The returned object from the intercepted method.
     */
    @AfterReturning(
            pointcut = "execution(* com.finanzen.api.application.service.user.CreateUserUseCase.create(..))",
            returning = "result"
    )
    public void logIamAudit(JoinPoint jp, Object result) {

        if (result instanceof User savedUser) {

            Long generatedId = savedUser.getId();
            String userEmail = savedUser.getEmail();

            IamAuditEntity iamAuditEntity = new IamAuditEntity(
                    null,
                    generatedId,
                    userEmail,
                    IamAuditActionType.USER_CREATED,
                    LocalDateTime.now()
            );

            repository.save(iamAuditEntity);
            System.out.println("[IAM AUDIT] -> Action " + IamAuditActionType.USER_CREATED + " registered in MongoDB for User ID: " + generatedId);
        } else {
            System.err.println("[IAM AUDIT] Failure: The Use Case return type does not match the expected User domain entity.");
        }
    }
}