package com.wallex.financial_platform.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ReservationRequestDTO(
        @NotNull @Positive BigDecimal reservedAmount,
        @NotNull String reason
) {}
