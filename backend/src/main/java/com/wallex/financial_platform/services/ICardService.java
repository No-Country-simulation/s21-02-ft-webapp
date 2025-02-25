package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.requests.RegisterCardRequestDTO;
import com.wallex.financial_platform.dtos.requests.DniRequestDTO;
import com.wallex.financial_platform.dtos.responses.CardResponseDTO;
import com.wallex.financial_platform.entities.Card;

import java.util.List;

public interface ICardService {
    CardResponseDTO createCard(RegisterCardRequestDTO cardRequestDTO);
    void deleteCard(Long id);
    List<CardResponseDTO> getAllCards();
    List<CardResponseDTO> getCardsByUserDni(DniRequestDTO dniRequestDTO);
}
