package com.finanzen.api.infrastructure.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.finanzen.api.application.exception.DuplicateEmailException;

/**
 * Global centralized exception handler for the REST controllers.
 * <p>
 * This class acts as a safety net, intercepting domain and application 
 * level exceptions and translating them into standardized, user-friendly 
 * HTTP responses (e.g., 409 Conflict).
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles DuplicateEmailException thrown during user registration.
     *
     * @param ex the intercepted exception containing the business error message.
     * @return a 409 Conflict response with the error message in the body.
     */
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<String> duplicateEmailException(DuplicateEmailException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
