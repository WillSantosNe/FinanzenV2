package com.finanzen.api.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when a core business rule/invariant is violated.
 * <p>
 * Maps to HttpStatus.BAD_REQUEST (400) because the request syntax might be correct,
 * but the business state prevents its execution (e.g., insufficient funds).
 * </p>
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}