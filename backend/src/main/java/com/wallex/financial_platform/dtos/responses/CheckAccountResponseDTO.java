package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.enums.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información de cuenta para verificación")
public record CheckAccountResponseDTO(
        @Schema(description = "CBU de la cuenta",
                example = "0000003100035587501234")
        String cbu,

        @Schema(description = "Alias de la cuenta",
                example = "mi.cuenta.wallex")
        String alias,

        @Schema(description = "Tipo de moneda",
                example = "ARS")
        CurrencyType currency,

        @Schema(description = "Nombre del titular",
                example = "Juan Pérez")
        String owner,

        @Schema(description = "DNI del titular",
                example = "12345678")
        String dni
) {
}