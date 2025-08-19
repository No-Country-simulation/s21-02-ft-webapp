package com.wallex.financial_platform.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import org.springframework.web.multipart.MultipartFile;

public record SuggestedReserveRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        MultipartFile icon
) {
}
