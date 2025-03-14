package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.enums.TransactionStatus;
import com.wallex.financial_platform.entities.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long transactionId,
        CheckAccountResponseDTO source,
        CheckAccountResponseDTO destination,
        LocalDateTime date,
        TransactionType type,
        TransactionStatus status,
        BigDecimal amount,
        String reason
) {
}
