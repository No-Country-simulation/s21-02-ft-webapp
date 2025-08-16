package com.wallex.financial_platform.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ReservationTypeRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        MultipartFile icon
) {
}
