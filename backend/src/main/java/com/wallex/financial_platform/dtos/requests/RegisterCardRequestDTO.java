package com.wallex.financial_platform.dtos.requests;

import com.wallex.financial_platform.entities.enums.CardType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


public record RegisterCardRequestDTO(
        @NotBlank(message = "El número de tarjeta no puede estar vacío")
        @Size(min = 16, max = 19, message = "El número de tarjeta debe tener entre 16 y 19 caracteres")
        String encryptedNumber,

        @NotNull(message = "El tipo de tarjeta no puede estar vacío")
        CardType type,

        @NotBlank(message = "El banco emisor no puede estar vacío")
        @Size(min = 2, max = 50, message = "El banco emisor debe tener entre 2 y 50 caracteres")
        String issuingBank,

        @Pattern(regexp = "^(0[1-9]|1[0-2])/(\\d{2})$", message = "La fecha de expiración debe seguir el formato MM/YY")
        String expirationDate,

        @NotBlank(message = "El CVV no puede estar vacío")
        @Pattern(regexp = "^\\d{3,4}$", message = "El CVV debe tener 3 o 4 dígitos")
        String encryptedCvv
) {
    public RegisterCardRequestDTO {
        validateExpirationDate(expirationDate);
    }

    private static void validateExpirationDate(String expirationDate) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
            YearMonth expiration = YearMonth.parse(expirationDate, formatter);
            if (expiration.isBefore(YearMonth.now())) {
                throw new IllegalArgumentException("La tarjeta está vencida");
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha de expiración inválido");
        }
    }
}