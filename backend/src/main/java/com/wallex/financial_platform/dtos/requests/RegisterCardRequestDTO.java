package com.wallex.financial_platform.dtos.requests;

import com.wallex.financial_platform.entities.enums.CardType;
import com.wallex.financial_platform.exceptions.card.CardExpiredException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Schema(description = "Datos para registrar una nueva tarjeta")
public record RegisterCardRequestDTO(
        @NotBlank(message = "El número de tarjeta no puede estar vacío")
        @Size(min = 16, max = 19, message = "El número de tarjeta debe tener entre 16 y 19 caracteres")
        @Schema(description = "Número de tarjeta", example = "4111111111111111", minLength = 16, maxLength = 19)
        String encryptedNumber,

        @NotNull(message = "El tipo de tarjeta no puede estar vacío")
        @Schema(description = "Tipo de tarjeta", example = "VISA")
        CardType type,

        @NotBlank(message = "El banco emisor no puede estar vacío")
        @Size(min = 2, max = 50, message = "El banco emisor debe tener entre 2 y 50 caracteres")
        @Schema(description = "Banco emisor", example = "Banco Galicia",  minLength = 2, maxLength = 50)
        String issuingBank,

        @Pattern(regexp = "^(0[1-9]|1[0-2])/(\\d{2})$", message = "La fecha de expiración debe seguir el formato MM/YY")
        @Schema(description = "Fecha de expiración (MM/YY)", example = "12/25")
        String expirationDate,

        @NotBlank(message = "El CVV no puede estar vacío")
        @Pattern(regexp = "^\\d{3,4}$", message = "El CVV debe tener 3 o 4 dígitos")
        @Schema(description = "CVV de la tarjeta", example = "123", minLength = 3, maxLength = 4)
        String encryptedCvv,

        @NotNull(message = "El balance no puede estar vacío")
        @Schema(description = "Saldo inicial", example = "10000.00")
        BigDecimal balance
) {
    // Constructor y validaciones se mantienen igual
    public RegisterCardRequestDTO {
        validateExpirationDate(expirationDate);
        validateBalance(balance);
    }

    private static void validateExpirationDate(String expirationDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiration = YearMonth.parse(expirationDate, formatter);
            if (expiration.isBefore(YearMonth.now())) {
                throw new CardExpiredException("La tarjeta está vencida");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha de expiración inválido");
        }
    }

    private static void validateBalance(BigDecimal balance) {
        if (balance != null && balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El balance no puede ser negativo");
        }
    }
}