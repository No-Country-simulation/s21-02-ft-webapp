package com.wallex.financial_platform.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Información de movimiento bancario")
public record MovementResponseDTO(
        @Schema(description = "ID del movimiento", example = "100")
        Long movementId,

        @Schema(description = "ID de la cuenta", example = "1")
        Long accountId,

        @Schema(description = "ID de la transacción relacionada", example = "50")
        Long transactionId,

        @Schema(description = "Descripción del movimiento", example = "Transferencia recibida")
        String description,

        @Schema(description = "Monto del movimiento", example = "1500.75")
        BigDecimal amount,

        @Schema(description = "Fecha y hora del movimiento", example = "2024-01-15T10:30:00")
        LocalDateTime movementDate,

        @Schema(description = "Nombre del usuario involucrado", example = "Juan Pérez")
        String userName,

        @Schema(description = "Tipo de transacción", example = "TRANSFER")
        String transactionType
) {
}