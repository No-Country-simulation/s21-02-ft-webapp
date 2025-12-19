package com.wallex.financial_platform.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Respuesta de verificación de saldo")
public record BalanceCheckResponseDTO(
        @Schema(description = "Indica si hay saldo suficiente", example = "true")
        boolean hasEnoughBalance,

        @Schema(description = "Saldo actual de la cuenta", example = "5000.75")
        BigDecimal currentBalance,

        @Schema(description = "Tipo de moneda", example = "ARS")
        String currency) {
}