package com.wallex.financial_platform.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


public record DniRequestDTO(@Pattern(regexp = "^\\d{8}$", message = "El DNI debe tener exactamente 8 dígitos") String dni) { }
