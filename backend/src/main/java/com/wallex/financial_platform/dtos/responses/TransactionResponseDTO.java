package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.enums.TransactionStatus;
import com.wallex.financial_platform.entities.enums.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponseDTO(
        Long transactionId,
        LocalDateTime date,
        TransactionType type,
        TransactionStatus status,
        BigDecimal amount,
        String reason
) {
}
