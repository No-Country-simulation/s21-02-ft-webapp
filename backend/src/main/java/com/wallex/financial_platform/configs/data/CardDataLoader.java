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
import java.util.ArrayList;
import java.util.List;

@Component
public class CardDataLoader {

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;

    private static final String PASSWORD_DEBIT = "123";
    private static final String PASSWORD_CREDIT = "222";

    public CardDataLoader(CardRepository cardRepository, UserRepository userRepository,
                          EncryptionService encryptionService, PasswordEncoder passwordEncoder) {
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.encryptionService = encryptionService;
    }

    public void load() {
        List<User> users = userRepository.findAll();
        if (users.size() < 5) {
            throw new IllegalStateException("No hay suficientes usuarios en la base de datos");
        }

        LocalDateTime date1 = LocalDateTime.of(2025, 2, 15, 10, 0);
        LocalDateTime date2 = LocalDateTime.of(2025, 2, 16, 12, 30);
        LocalDateTime date3 = LocalDateTime.of(2025, 2, 17, 15, 45);
        LocalDateTime date4 = LocalDateTime.of(2025, 2, 18, 9, 15);
        LocalDateTime date5 = LocalDateTime.of(2025, 2, 18, 14, 0);
        LocalDateTime date6 = LocalDateTime.of(2025, 2, 19, 11, 30);

        List<Card> cards = List.of(
                createCard(users.get(0), "5031755734530604", CardType.DEBIT, users.get(0), "11/30", PASSWORD_CREDIT, 500000.00, date1),
                createCard(users.get(1), "4509953566233704", CardType.DEBIT, users.get(1), "11/30", PASSWORD_CREDIT, 1000000.00, date2),
                createCard(users.get(2), "371180303257522", CardType.DEBIT, users.get(2), "11/30", PASSWORD_CREDIT, 750000.50, date3),
                createCard(users.get(3), "5287338310253304", CardType.DEBIT, users.get(3), "11/30", PASSWORD_DEBIT, 200000.75, date4),
                createCard(users.get(4), "4002768694395619", CardType.DEBIT, users.get(4), "11/30", PASSWORD_DEBIT, 120000.25, date5),
                createCard(users.get(4), "9988776655443322", CardType.CREDIT, users.get(4), "11/30", PASSWORD_CREDIT, 250000.00, date6)
        );

        cardRepository.saveAll(cards);
    }

    private Card createCard(User user, String cardNumber, CardType cardType, User bankName, String expiryDate,
                            String password, double initialBalance, LocalDateTime registrationDate) {
        return new Card(
                null,
                user,
                encryptionService.encrypt(cardNumber),
                cardType,
                bankName,
                expiryDate,
                encryptionService.encrypt(password),
                BigDecimal.valueOf(initialBalance),
                registrationDate,
                new ArrayList<>()
        );
    }
}