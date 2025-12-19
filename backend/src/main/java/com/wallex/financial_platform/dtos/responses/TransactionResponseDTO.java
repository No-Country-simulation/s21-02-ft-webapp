package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.enums.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Respuesta de transacción")
public record TransactionResponseDTO(
        @Schema(description = "ID de la transacción", example = "100")
        Long transactionId,

        @Schema(description = "Fecha y hora de la transacción", example = "2024-01-15T10:30:00")
        LocalDateTime transactionDate,

        @Schema(description = "CBU de la cuenta origen", example = "0000003100035587501234")
        String sourceAccount,

        @Schema(description = "CBU de la cuenta destino", example = "0000003100035587505678")
        String destinationAccount,

        @Schema(description = "Monto de la transacción", example = "1500.50")
        BigDecimal amount,

        @Schema(description = "Motivo de la transacción", example = "Pago de servicios")
        String reason,

        @Schema(description = "Tipo de transacción")
        TransactionType transactionType) {
}