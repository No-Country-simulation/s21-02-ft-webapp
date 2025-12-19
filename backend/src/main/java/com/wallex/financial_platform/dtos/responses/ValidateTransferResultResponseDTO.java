package com.wallex.financial_platform.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta de validación de cuenta")
public record ValidateTransferResultResponseDTO(
        @Schema(description = "Indica si la cuenta es válida", example = "true")
        boolean isValid,

        @Schema(description = "Nombre del titular de la cuenta", example = "Juan Pérez")
        String accountName,

        @Schema(description = "Tipo de identificación", example = "CBU")
        String type) {
}