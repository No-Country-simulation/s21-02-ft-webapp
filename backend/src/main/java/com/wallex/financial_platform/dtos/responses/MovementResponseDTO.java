package com.wallex.financial_platform.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementResponseDTO(
        Long movementId,
        Long accountId,
        Long transactionId,
        String description,
        BigDecimal amount,
        LocalDateTime movementDate,
        String userName,
        String transactionType
) {
}
