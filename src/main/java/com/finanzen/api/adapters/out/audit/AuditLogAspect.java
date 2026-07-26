package com.finanzen.api.adapters.out.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Aspect
@Component
@AllArgsConstructor
public class AuditLogAspect {

    private final MongoAuditRepository mongoAuditRepository;

    // Pointcut - intercepta qualquer execução sinalizada
    @AfterReturning(
            pointcut = "execution(* com.finanzen.api.application.service.account.UpdateAccountBalanceUseCase.execute(..))",
            returning = "result"
    )
    public void logTransaction(JoinPoint joinPoint, Object result) {

        // Joinpoint - capturando parâmetros enviados para o execute()
        Object[] args = joinPoint.getArgs();

        // Extraindo parametros
        Long accountId = (Long) args[0];
        BigDecimal amount = (BigDecimal) args[1];
        String userEmail = (String) args[2];

        AuditLogEntity auditLogEntity = new AuditLogEntity(null, accountId, amount, userEmail, LocalDateTime.now());

        mongoAuditRepository.save(auditLogEntity);

        System.out.println("[AUDIT LOG] -> Rastro de auditoria salvo no MongoDB silenciosamente!");
    }
}