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
    private static final String PASSWORD_CREDIT = "222";

    public CardDataLoader(CardRepository cardRepository, UserRepository userRepository,
                          EncryptionService encryptionService, PasswordEncoder passwordEncoder) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
        this.passwordEncoder = passwordEncoder;
    }

    public void load() {
        List<User> users = userRepository.findAll();  // Obtenemos todos los usuarios
        if (users.size() < 4) {
            throw new IllegalStateException("No hay suficientes usuarios en la base de datos");
        }

        List<Card> cards = List.of(
                createCard(users.get(0), "1234567890123456", CardType.DEBIT, "Banco Nación", "12/25", PASSWORD_DEBIT, 500000.00),
                createCard(users.get(1), "9876543210987654", CardType.DEBIT, "Banco Galicia", "08/24", PASSWORD_DEBIT, 1000000.00),
                createCard(users.get(2), "8765432109876543", CardType.DEBIT, "Banco Supervielle", "05/23", PASSWORD_DEBIT, 750000.50),
                createCard(users.get(3), "5432109876543210", CardType.DEBIT, "Banco Ciudad", "02/27", PASSWORD_DEBIT, 200000.75)
/*                createCard(users.get(4), "1122334455667788", CardType.DEBIT, "Banco Santander", "11/26", PASSWORD_DEBIT, 120000.25),
                createCard(users.get(4), "9988776655443322", CardType.CREDIT, "Banco Macro", "03/28", PASSWORD_CREDIT, 250000.00)*/
        );

        cardRepository.saveAll(cards);
    }

    private Card createCard(User user, String cardNumber, CardType cardType, String bankName, String expiryDate,
                            String password, double initialBalance) {
        return new Card(
                null,
                user,
                encryptionService.encrypt(cardNumber),
                cardType,
                bankName,
                expiryDate,
                passwordEncoder.encode(password),
                BigDecimal.valueOf(initialBalance),
                LocalDateTime.now()
        );
    }
}
