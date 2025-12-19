package com.wallex.financial_platform.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Solicitud para depósito con número de tarjeta")
public record DepositRequestDTO(
        @Min(value = 1, message = "amount must be more than 1 unit")
        @Schema(description = "Monto a depositar",
                example = "5000.00")
        BigDecimal amount,

        @NotNull(message = "Número de tarjeta es requerido")
        @Size(min = 16, max = 19, message = "El número de tarjeta debe tener entre 16 y 19 dígitos")
        @Schema(description = "Número de tarjeta",
                example = "4111111111111111",
                minLength = 16,
                maxLength = 19)
        String cardNumber
) {
}