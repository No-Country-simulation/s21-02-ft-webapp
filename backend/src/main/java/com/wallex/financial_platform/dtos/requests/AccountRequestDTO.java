package com.wallex.financial_platform.dtos.requests;

import com.wallex.financial_platform.entities.enums.CurrencyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Solicitud para crear cuenta")
public record AccountRequestDTO(
        @NotNull(message = "currency is required")
        @Schema(description = "Tipo de moneda", example = "ARS")
        CurrencyType currency
){}