package com.finanzen.api.domain.exceptions;

public class DomainRuleException extends RuntimeException {
    public DomainRuleException(String message) {
        super(message);
    }
}