package com.wallex.financial_platform.services.utils;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.exceptions.account.AccountErrorException;
import com.wallex.financial_platform.exceptions.transaction.InsufficientFundsException;

import java.math.BigDecimal;

public final class AccountValidationUtils {

    public static void validateAccountOwnership(Account account, User authenticatedUser) {
        if (!account.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccountErrorException("No estás autorizado para operar esta cuenta.");
        }
    }

    public static void validateSufficientFunds(Account account, BigDecimal amount) {
        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes para realizar la operación");
        }
    }

    public static void validateAccountStatus(Account account) {
        if (!account.getActive()) {
            throw new AccountErrorException("La cuenta no está activa");
        }
    }

    public static void validateCurrencyCompatibility(Account sourceAccount, Account destinationAccount) {
        if (!sourceAccount.getCurrency().equals(destinationAccount.getCurrency())) {
            throw new AccountErrorException("Las cuentas deben tener la misma moneda para realizar la operación");
        }
    }
}
