package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Card;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.CardType;
import com.wallex.financial_platform.repositories.CardRepository;
import com.wallex.financial_platform.repositories.UserRepository;
import com.wallex.financial_platform.services.utils.EncryptionService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class CardDataLoader {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final PasswordEncoder passwordEncoder;

    private static final String PASSWORD_DEBIT = "123";

    public CardDataLoader(CardRepository cardRepository, UserRepository userRepository,
                          EncryptionService encryptionService, PasswordEncoder passwordEncoder) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.passwordEncoder = passwordEncoder;
    }

    public void load() {
        List<User> users = userRepository.findAll();
        if (users.size() < 4) {
            throw new IllegalStateException("No hay suficientes usuarios en la base de datos");
        }

        List<Card> cards = List.of(
                createCard(users.get(0), "1234567890123456", "Banco Nación", "12/25", 500000.00),
                createCard(users.get(1), "9876543210987654", "Banco Galicia", "08/24", 1000000.00),
                createCard(users.get(2), "8765432109876543", "Banco Supervielle", "05/23", 750000.50),
                createCard(users.get(3), "5432109876543210", "Banco Ciudad", "02/27", 200000.75)
        );

        cardRepository.saveAll(cards);
    }

    private Card createCard(User user, String cardNumber, String bankName, String expiryDate,
                            double initialBalance) {
        return new Card(
                null,
                user,
                encryptionService.encrypt(cardNumber),
                CardType.DEBIT,
                bankName,
                expiryDate,
                encryptionService.encrypt(CardDataLoader.PASSWORD_DEBIT),
                BigDecimal.valueOf(initialBalance),
                LocalDateTime.now()
        );
    }
}
