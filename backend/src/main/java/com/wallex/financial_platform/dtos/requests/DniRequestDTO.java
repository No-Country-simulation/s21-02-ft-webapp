package com.wallex.financial_platform.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;

@Schema(description = "Solicitud por DNI")
public record DniRequestDTO(
        @Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener 8 dígitos")
        @Schema(description = "DNI de 8 dígitos", example = "12345678")
        String dni
) {}