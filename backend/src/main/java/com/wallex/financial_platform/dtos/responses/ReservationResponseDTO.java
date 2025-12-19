package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.enums.ReservationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Información de reserva de fondos")
public record ReservationResponseDTO(
        @Schema(description = "ID de la reserva", example = "10")
        Long reservationId,

        @Schema(description = "ID de la cuenta", example = "1")
        Long accountId,

        @Schema(description = "Monto reservado", example = "1500.75")
        BigDecimal reservedAmount,

        @Schema(description = "Fecha de creación", example = "2024-01-15T10:30:00")
        LocalDateTime creationDate,

        @Schema(description = "Estado de la reserva", example = "ACTIVE")
        ReservationStatus status,

        @Schema(description = "Motivo de la reserva", example = "Compra en línea pendiente")
        String reason
) {
}