package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.enums.CardType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CardResponseDTO(
 CardType type,
 String encryptedNumber,
 String issuingBank,
 String expirationDate,
 BigDecimal balance,
 String cvv,
 LocalDateTime registrationDate
) { }
