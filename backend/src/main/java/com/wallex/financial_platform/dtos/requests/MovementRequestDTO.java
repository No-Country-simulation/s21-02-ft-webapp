package com.wallex.financial_platform.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovementRequestDTO(
         Long accountId,
         Long transactionId,
         String description,
         BigDecimal amount,
         LocalDateTime movementDate
) {
}
