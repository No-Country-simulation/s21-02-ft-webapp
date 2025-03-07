package com.wallex.financial_platform.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DepositRequestDTO(
        @Min(value = 1, message = "amount must be more than 1 unit")
        BigDecimal amount,

        @NotNull(message = "Número de tarjeta es requerido")
        String cardNumber
) {

}
