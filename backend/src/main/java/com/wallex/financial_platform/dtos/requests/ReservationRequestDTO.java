package com.wallex.financial_platform.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wallex.financial_platform.entities.enums.TypeReservation;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ReservationRequestDTO(
        @NotNull Long accountId,
        @NotNull @Positive BigDecimal reservedAmount,
        @NotNull TypeReservation type
) {}
