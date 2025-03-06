package com.wallex.financial_platform.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequestDTO(
        @NotNull(message = "sourceAccountId is required")
        Long sourceAccountId,

        @NotNull(message = "destinationIdentifier is required")
        @Size(min = 27, max = 27, message = "destinationIdentifier must be a valid CBU")
        String destinationIdentifier,

        @Min(value = 1, message = "amount must be more than 1 unit")
        BigDecimal amount,

        @NotNull(message = "reason is required")
        String reason) {
}
