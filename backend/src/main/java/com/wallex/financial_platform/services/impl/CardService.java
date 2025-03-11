package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.requests.RegisterCardRequestDTO;
import com.wallex.financial_platform.dtos.requests.DniRequestDTO;
import com.wallex.financial_platform.dtos.responses.CardResponseDTO;
import com.wallex.financial_platform.entities.Card;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.CardType;
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
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardService implements ICardService {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final UserContextService userContextService;
    private final EncryptionService encryptionService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public CardResponseDTO createCard(RegisterCardRequestDTO cardRequestDTO) {
        String encryptedNumber = encryptionService.encrypt(cardRequestDTO.encryptedNumber());
        if (cardRepository.existsByEncryptedNumber(encryptedNumber)) {
            throw new CardAlreadyExistsException("La tarjeta ya existe en el sistema");
        }

        User user = userContextService.getAuthenticatedUser();
        if (user == null) {
            throw new UserNotFoundException("Usuario autenticado no encontrado.");
        }
        User provider = userRepository.findByFullNameContaining(cardRequestDTO.issuingBank())
                .orElseThrow(()-> new UserNotFoundException("Card provider not found"));

        Card card = new Card();
        card.setEncryptedNumber(encryptionService.encrypt(cardRequestDTO.encryptedNumber()));
        card.setType(cardRequestDTO.type());
        card.setProvider(provider);
        card.setExpirationDate(cardRequestDTO.expirationDate());
        card.setEncryptedCvv(encryptionService.encrypt(cardRequestDTO.encryptedCvv()));
        card.setBalance(cardRequestDTO.balance());
        card.setUser(user);

        cardRepository.save(card);

        notificationService.notifyUser(user,
                "✨ Tarjeta registrada con éxito en tu cuenta",
                "🎉 ¡Tu tarjeta ha sido agregada correctamente! Ahora puedes gestionar tus pagos y consultar tu saldo desde nuestra plataforma.");

        return convertToDTO(card);
    }

    @Override
    public List<CardResponseDTO> getCardsByUserDni(DniRequestDTO dniRequestDTO) {
        User user = userRepository.findByDni(dniRequestDTO.dni())
                .orElseThrow(() -> new UserNotFoundException("No existe usuario asociado al DNI: " + dniRequestDTO.dni()));

        List<Card> cards = cardRepository.findByUserId(user.getId());
        if (cards.isEmpty()) {
            throw new CardNotFoundException("No se encontraron tarjetas asociadas a este usuario.");
        }
        return mapCardsToDTOs(cards);
    }

    @Override
    public List<String> getAllCardTypes() {
        return Arrays.stream(CardType.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllProviders() {
        return userRepository.findByIsCardProvider(true).stream().map(User::getFullName).toList();
    }

    @Override
    public List<CardResponseDTO> getAllCardsByUserOnline() {
        List<Card> cards = cardRepository.findByUserId(userContextService.getAuthenticatedUser().getId());
        if (cards.isEmpty()) {
            throw new CardNotFoundException("No hay tarjetas asociadas a este usuario.");
        }
        return mapCardsToDTOs(cards);
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("Tarjeta con ID " + cardId + " no encontrada."));
        if (!userContextService.getAuthenticatedUser().getId().equals(card.getUser().getId())) {
            throw new UnauthorizedCardDeletionException("No está autorizado para eliminar esta tarjeta.");
        }
        cardRepository.delete(card);
    }

    private Card prepareCardForDTO(Card card) {
        User authenticatedUser = userContextService.getAuthenticatedUser();
        if (authenticatedUser != null && authenticatedUser.getId().equals(card.getUser().getId())) {
            card.setEncryptedNumber(encryptionService.decrypt(card.getEncryptedNumber()));
            card.setEncryptedCvv(encryptionService.decrypt(card.getEncryptedCvv()));
        }
        return card;
    }

    private List<CardResponseDTO> mapCardsToDTOs(List<Card> cards) {
        return cards.stream()
                .map(this::prepareCardForDTO)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CardResponseDTO convertToDTO(Card card) {
        return new CardResponseDTO(
                card.getType(),
                card.getEncryptedNumber(),
                card.getProvider().getFullName(),
                card.getExpirationDate(),
                card.getBalance(),
                card.getEncryptedCvv(),
                card.getRegistrationDate()
        );
    }
}
