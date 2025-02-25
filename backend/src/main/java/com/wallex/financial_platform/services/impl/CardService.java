package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.requests.RegisterCardRequestDTO;
import com.wallex.financial_platform.dtos.requests.DniRequestDTO;
import com.wallex.financial_platform.dtos.responses.CardResponseDTO;
import com.wallex.financial_platform.entities.Card;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.exceptions.auth.UserNotFoundException;
import com.wallex.financial_platform.exceptions.card.CardAlreadyExistsException;
import com.wallex.financial_platform.exceptions.card.CardNotFoundException;
import com.wallex.financial_platform.exceptions.card.UnauthorizedCardDeletionException;
import com.wallex.financial_platform.repositories.CardRepository;
import com.wallex.financial_platform.repositories.UserRepository;
import com.wallex.financial_platform.services.ICardService;
import com.wallex.financial_platform.services.utils.EncryptionService;
import com.wallex.financial_platform.services.utils.UserContextService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CardService implements ICardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserContextService userContextService;
    private final EncryptionService encryptionService;

    @Override
    @Transactional
    public CardResponseDTO createCard(RegisterCardRequestDTO cardRequestDTO) {
        if (this.cardRepository.existsByEncryptedNumber(cardRequestDTO.encryptedNumber())) {
            throw new CardAlreadyExistsException("La tarjeta ya existe en el sistema");
        }

        User user = this.userContextService.getAuthenticatedUser();
        if (user == null) {
            throw new UserNotFoundException("Usuario autenticado no encontrado.");
        }

        Card card = new Card();
        card.setEncryptedNumber(encryptionService.encrypt(cardRequestDTO.encryptedNumber()));
        card.setType(cardRequestDTO.type());
        card.setIssuingBank(cardRequestDTO.issuingBank());
        card.setExpirationDate(cardRequestDTO.expirationDate());
        card.setEncryptedCvv(passwordEncoder.encode(cardRequestDTO.encryptedCvv()));
        card.setUser(user);

        this.cardRepository.save(card);

        return convertToDTO(card);
    }

    @Override
    public List<CardResponseDTO> getCardsByUserDni(DniRequestDTO dniRequestDTO) {
        User user = this.userRepository.findByDni(dniRequestDTO.dni())
                .orElseThrow(() -> new UserNotFoundException("No existe usuario asociado al DNI: " + dniRequestDTO.dni()));

        List<Card> cards = this.cardRepository.findByUserId(user.getId());
        if (cards.isEmpty()) {
            throw new CardNotFoundException("No se encontraron tarjetas asociadas a este usuario.");
        }

        return cards.stream()
                .map(card -> {
                    if (userContextService.getAuthenticatedUser().getId().equals(card.getUser().getId())) {
                        card.setEncryptedNumber(encryptionService.decrypt(card.getEncryptedNumber()));
                    }
                    return convertToDTO(card);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        Card card = this.cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Tarjeta con ID " + cardId + " no encontrada."));
        if(!userContextService.getAuthenticatedUser().getId().equals(card.getUser().getId())) {
            throw new UnauthorizedCardDeletionException("No está autorizado para eliminar esta tarjeta.");
        }
        this.cardRepository.delete(card);
    }

    @Override
    public List<CardResponseDTO> getAllCards() {
        List<Card> cards = this.cardRepository.findAll();
        if (cards.isEmpty()) {
            throw new CardNotFoundException("No hay tarjetas registradas en el sistema.");
        }

        return cards.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CardResponseDTO convertToDTO(Card card) {
        return new CardResponseDTO(
                card.getType(),
                card.getEncryptedNumber(),
                card.getIssuingBank(),
                card.getExpirationDate(),
                card.getRegistrationDate()
        );
    }


}
