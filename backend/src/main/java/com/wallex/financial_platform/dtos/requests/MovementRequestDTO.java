package com.wallex.financial_platform.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Solicitud para crear un movimiento")
public record MovementRequestDTO(
        @Schema(description = "ID de la cuenta",
                example = "1")
        Long accountId,

        @Schema(description = "ID de la transacción relacionada",
                example = "100")
        Long transactionId,

        @Schema(description = "Descripción del movimiento",
                example = "Transferencia recibida")
        String description,

        @Schema(description = "Monto del movimiento",
                example = "1500.75")
        BigDecimal amount,

        @Schema(description = "Fecha y hora del movimiento",
                example = "2024-01-15T10:30:00")
        LocalDateTime movementDate
) {
}