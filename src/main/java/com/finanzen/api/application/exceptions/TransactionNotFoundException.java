package com.finanzen.api.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Application-level exception thrown when a requested transaction cannot be found.
 * <p>
 * In a strict Hexagonal Architecture, the core business logic must not throw
 * framework-specific exceptions (such as JPA's {@code EntityNotFoundException}).
 * By using this custom exception, we maintain the purity of the application layer
 * and allow the GlobalExceptionHandler to map this business error to the correct
 * HTTP status (e.g., 404 Not Found) at the web boundary.
 * </p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends RuntimeException {

    /**
     * Constructs a new TransactionNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining which transaction was not found.
     */
    public TransactionNotFoundException(String message) {
        super(message);
    }
}