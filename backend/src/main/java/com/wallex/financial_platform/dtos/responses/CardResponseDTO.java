package com.wallex.financial_platform.dtos.responses;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.CardType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public record CardResponseDTO(
 CardType type,
 String encryptedNumber,
 String issuingBank,
 String expirationDate,
 LocalDateTime registrationDate
) { }
