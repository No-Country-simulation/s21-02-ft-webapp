package com.wallex.financial_platform.exceptions.transaction;

public class TransactionErrorException extends RuntimeException {
    public TransactionErrorException(String message) {
        super(message);
    }

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
