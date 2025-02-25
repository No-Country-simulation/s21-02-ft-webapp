package com.wallex.financial_platform.repositories;

import com.wallex.financial_platform.entities.Card;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByUserId(Long userId);
    boolean existsByEncryptedNumber(String encryptedNumber);

}
