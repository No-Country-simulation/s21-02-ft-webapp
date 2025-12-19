package com.wallex.financial_platform.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Schema(description = "Solicitud para crear una reserva de fondos")
public record ReservationRequestDTO(
        @NotNull @Positive
        @Schema(description = "Monto a reservar (debe ser mayor a 0)",
                example = "1500.75")
        BigDecimal reservedAmount,

        @NotNull
        @Schema(description = "Motivo o descripción de la reserva",
                example = "Reserva para compra en línea")
        String reason
) {}