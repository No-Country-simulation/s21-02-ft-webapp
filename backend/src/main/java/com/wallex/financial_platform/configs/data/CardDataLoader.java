package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Card;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.CardType;
import com.wallex.financial_platform.repositories.CardRepository;
import com.wallex.financial_platform.repositories.UserRepository;
import com.wallex.financial_platform.services.utils.EncryptionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CardDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(CardDataLoader.class);

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final EncryptionService encryptionService;
    private final PasswordEncoder passwordEncoder;

    private static final String PASSWORD_DEBIT = "123";

    public void load() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            logger.warn("⚠️ No hay usuarios para crear tarjetas");
            return;
        }

        // Datos de tarjetas de prueba
        List<CardData> testCards = List.of(
                new CardData("1234567890123456", "Banco Nación", "12/25", 500000.00),
                new CardData("9876543210987654", "Banco Galicia", "08/24", 1000000.00),
                new CardData("8765432109876543", "Banco Supervielle", "05/23", 750000.50),
                new CardData("5432109876543210", "Banco Ciudad", "02/27", 200000.75)
        );

        List<Card> cardsToCreate = new ArrayList<>();
        int created = 0;
        int skipped = 0;

        for (int i = 0; i < Math.min(users.size(), testCards.size()); i++) {
            CardData cardData = testCards.get(i);
            User user = users.get(i);

            // Verificar si la tarjeta ya existe para este usuario
            String encryptedNumber = encryptionService.encrypt(cardData.cardNumber);

            boolean cardExists = cardRepository.existsByEncryptedNumberAndUser(encryptedNumber, user);

            if (!cardExists) {
                cardsToCreate.add(createCard(
                        user,
                        cardData.cardNumber,
                        cardData.bankName,
                        cardData.expiryDate,
                        cardData.initialBalance
                ));
                created++;
            } else {
                skipped++;
            }
        }

        if (!cardsToCreate.isEmpty()) {
            try {
                cardRepository.saveAll(cardsToCreate);
                logger.info("✅ {} tarjetas creadas, {} omitidas (ya existían)", created, skipped);
            } catch (Exception e) {
                logger.error("❌ Error al guardar tarjetas: {}", e.getMessage());
            }
        } else {
            logger.info("📝 Todas las tarjetas ya existen, omitiendo creación");
        }
    }

    private Card createCard(User user, String cardNumber, String bankName,
                            String expiryDate, double initialBalance) {
        return new Card(
                null,
                user,
                encryptionService.encrypt(cardNumber),
                CardType.DEBIT,
                bankName,
                expiryDate,
                encryptionService.encrypt(PASSWORD_DEBIT),
                BigDecimal.valueOf(initialBalance),
                LocalDateTime.now()
        );
    }

    // Clase auxiliar para datos de tarjeta
    private static class CardData {
        String cardNumber;
        String bankName;
        String expiryDate;
        double initialBalance;

        CardData(String cardNumber, String bankName, String expiryDate, double initialBalance) {
            this.cardNumber = cardNumber;
            this.bankName = bankName;
            this.expiryDate = expiryDate;
            this.initialBalance = initialBalance;
        }
    }
}