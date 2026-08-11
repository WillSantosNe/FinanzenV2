package com.finanzen.api.application.exceptions;

/**
 * Exception thrown when a core business rule/invariant is violated.
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}