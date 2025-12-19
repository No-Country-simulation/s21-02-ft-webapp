package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.enums.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Respuesta de información de cuenta")
public record AccountResponseDTO(
        @Schema(description = "ID de la cuenta", example = "1")
        Long accountId,

        @Schema(description = "CBU de la cuenta", example = "0000003100035587501234")
        String cbu,

        @Schema(description = "Alias de la cuenta", example = "mi.cuenta.wallex")
        String alias,

        @Schema(description = "Tipo de moneda")
        CurrencyType currency,

        @Schema(description = "Saldo disponible", example = "5000.75")
        BigDecimal balance,

        @Schema(description = "Saldo reservado", example = "1000.00")
        BigDecimal reservedBalance
) {}