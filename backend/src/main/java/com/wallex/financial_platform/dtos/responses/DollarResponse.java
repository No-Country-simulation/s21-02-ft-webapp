package com.wallex.financial_platform.dtos.responses;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Información de tipo de cambio del dólar")
public record DollarResponse(
        @Schema(description = "Tipo de moneda",
                example = "dolar")
        String moneda,

        @Schema(description = "Tipo de cotización",
                example = "oficial")
        String casa,

        @Schema(description = "Nombre descriptivo",
                example = "Dólar Oficial")
        String nombre,

        @Schema(description = "Precio de compra",
                example = "350.50")
        BigDecimal compra,

        @Schema(description = "Precio de venta",
                example = "365.75")
        BigDecimal venta,

        @Schema(description = "Fecha de última actualización",
                example = "2024-01-15T10:30:00")
        String fechaActualizacion
) {
}