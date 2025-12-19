package com.wallex.financial_platform.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Información de tipo de reserva sugerida")
public record SuggestedReserveResponseDTO(
        @Schema(description = "ID del tipo de reserva", example = "5")
        Long reservationTypeId,

        @Schema(description = "Nombre del tipo", example = "Vacaciones")
        String name,

        @Schema(description = "URL del icono", example = "https://cdn.wallex.com/icons/vacations.png")
        String iconUrl
) {}