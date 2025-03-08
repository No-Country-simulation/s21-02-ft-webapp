package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.entities.Account;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionService {
    TransactionResponseDTO createTransferTransaction(Account sourceAccount, Account destinationAccount, BigDecimal amount, String reason);
    TransactionResponseDTO createDepositTransaction(Account account, BigDecimal amount, String cardNumber);
    List<TransactionResponseDTO> getTransactionByAccount(Long id);

}
