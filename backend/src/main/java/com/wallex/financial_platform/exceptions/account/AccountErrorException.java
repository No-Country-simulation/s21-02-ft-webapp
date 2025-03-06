package com.wallex.financial_platform.exceptions.account;

public class AccountErrorException extends RuntimeException {
    public AccountErrorException(String message) {
        super(message);
    }
}
