package com.wallex.financial_platform.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequestDTO2(
        @Size(min=27, max = 27, message = "source Cbu not valid") String sourceCbu,
        @Size(min=27, max = 27, message = "destination Cbu not valid") String destinationCbu,
        @Min(value=1, message="amount must be more than 1 unit") BigDecimal amount,
        @NotNull String reason
) {}
