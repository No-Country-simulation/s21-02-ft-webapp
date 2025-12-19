package com.wallex.financial_platform.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "Solicitud para crear tipo de reserva sugerida")
public record SuggestedReserveRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        @Schema(description = "Nombre del tipo de reserva", example = "Vacaciones", required = true)
        String name,

        @Schema(description = "Icono/imagen para representar el tipo de reserva", required = true)
        MultipartFile icon
) {
}