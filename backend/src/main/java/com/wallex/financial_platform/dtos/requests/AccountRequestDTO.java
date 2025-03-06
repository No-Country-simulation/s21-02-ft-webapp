package com.wallex.financial_platform.dtos.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wallex.financial_platform.entities.enums.CurrencyType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AccountRequestDTO(
        @NotNull(message = "El tipo de tarjeta no puede estar vacío")
        CurrencyType currency

       /* @JsonProperty(required = false)
        @Size(min =22, message="El alias debe tener al menos 22 caracteres")
        String cbu,

        @JsonProperty(required = false)
        @Pattern(regexp = "^[a-zA-Z]+\\.[a-zA-Z]+\\.[a-zA-Z]+$", message = "alias not valid")
        String alias*/
){}
