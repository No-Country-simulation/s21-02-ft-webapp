package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;

public interface ITransactionService {
    TransactionResponseDTO getById(Long transactionId);

}
