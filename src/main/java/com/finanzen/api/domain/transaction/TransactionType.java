package com.finanzen.api.domain.transaction;

/**
 * Enumeration defining the nature of a financial transaction.
 * <p>
 * This pure domain enum is used to classify whether money is coming into
 * or going out of the user's account.
 * </p>
 */
public enum TransactionType {

    /**
     * Represents a positive cash flow (money entering the account).
     */
    INCOME,

    /**
     * Represents a negative cash flow (money leaving the account).
     */
    EXPENSE
}