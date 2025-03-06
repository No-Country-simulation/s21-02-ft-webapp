package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.entities.Account;

import java.math.BigDecimal;

public interface ITransactionService {
    //TransactionResponseDTO getById(Long transactionId);
    //TransactionResponseDTO save(TransactionRequestDTO transaction);
    TransactionResponseDTO createTransferTransaction(Account sourceAccount, Account destinationAccount, BigDecimal amount, String reason);
    TransactionResponseDTO createDepositTransaction(Account account, BigDecimal amount, String cardNumber);
}
