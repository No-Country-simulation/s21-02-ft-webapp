package com.wallex.financial_platform.dtos.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Solicitud para transferencia")
public record TransferRequestDTO(
        @NotNull(message = "destinationIdentifier is required")
        @Size(min = 6, max = 22, message = "destinationIdentifier must be a valid CBU")
        @Schema(description = "CBU o Alias de la cuenta destino", example = "0000003100035587501234")
        String destinationIdentifier,

        @Min(value = 1, message = "amount must be more than 1 unit")
        @Schema(description = "Monto a transferir", example = "1500.50")
        BigDecimal amount,

        @NotNull(message = "reason is required")
        @Schema(description = "Motivo de la transferencia", example = "Pago de factura")
        String reason) {
}