package com.wallex.financial_platform.exceptions.card;

public class UnauthorizedCardDeletionException extends RuntimeException {
    public UnauthorizedCardDeletionException(String message) {
        super(message);
    }
}
