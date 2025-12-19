package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Información de tarjeta")
public record CardResponseDTO(
        @Schema(description = "Tipo de tarjeta", example = "VISA")
        CardType type,

        @Schema(description = "Número de tarjeta encriptado", example = "************1234")
        String encryptedNumber,

        @Schema(description = "Banco emisor", example = "Banco Nación")
        String issuingBank,

        @Schema(description = "Fecha de expiración", example = "12/25")
        String expirationDate,

        @Schema(description = "Saldo disponible", example = "15000.50")
        BigDecimal balance,

        @Schema(description = "CVV encriptado", example = "***")
        String encryptedCvv,

        @Schema(description = "Fecha de registro", example = "2024-01-15T10:30:00")
        LocalDateTime registrationDate
) { }