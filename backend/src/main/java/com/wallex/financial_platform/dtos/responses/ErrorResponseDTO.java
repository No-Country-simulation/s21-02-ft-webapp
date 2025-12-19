package com.wallex.financial_platform.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Respuesta de error estándar")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponseDTO(
        @Schema(description = "Fecha y hora del error", example = "2024-01-15T10:30:00")
        LocalDateTime timestamp,

        @Schema(description = "Código de estado HTTP", example = "400")
        int status,

        @Schema(description = "Tipo de error", example = "Bad Request")
        String error,

        @Schema(description = "Mensaje descriptivo del error", example = "Validation failed")
        String message,

        @Schema(description = "Ruta donde ocurrió el error", example = "/api/accounts")
        String path,

        @Schema(description = "Lista de errores de validación (opcional)")
        List<String> errors
) {
    public ErrorResponseDTO(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }

    public ErrorResponseDTO(int status, String error, String message, String path, List<String> errors) {
        this(LocalDateTime.now(), status, error, message, path, errors);
    }
}