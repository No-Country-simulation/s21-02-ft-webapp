package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.CurrencyType;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(AccountDataLoader.class);
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public void load() {
        List<User> userList = userRepository.findAll();

        if (userList.isEmpty()) {
            logger.warn("⚠️ No hay usuarios disponibles para crear cuentas");
            return;
        }

        List<Account> accountList = new ArrayList<>();

        // Verificar si las cuentas ya existen antes de crearlas
        boolean accountsExist = checkIfDefaultAccountsExist();

        if (accountsExist) {
            logger.info("📝 Las cuentas predeterminadas ya existen, omitiendo creación");
            return;
        }

        // Datos de cuentas de prueba
        List<AccountData> testAccounts = List.of(
                new AccountData("1231234900000000000001", "orange.cactus.wave",
                        new BigDecimal("2500"), BigDecimal.ZERO, CurrencyType.USD),
                new AccountData("1231234900000000000002", "silver.pixel.turbo",
                        new BigDecimal("250000"), new BigDecimal("500"), CurrencyType.ARS),
                new AccountData(generateUniqueCBU(3), "velvet.shadow.coffee",
                        new BigDecimal("150000"), new BigDecimal("3000"), CurrencyType.ARS),
                new AccountData("1231234900000000000004", "aqua.flame.breeze",
                        new BigDecimal("500000"), new BigDecimal("1000"), CurrencyType.ARS),
                new AccountData("1231234900000000000005", "crystal.echo.spark",
                        new BigDecimal("350000"), new BigDecimal("700"), CurrencyType.ARS),
                new AccountData("1231234900000000000006", "rapid.fox.lake",
                        new BigDecimal("350000"), new BigDecimal("700"), CurrencyType.ARS)
        );

        // Distribuir cuentas entre usuarios disponibles
        for (int i = 0; i < testAccounts.size(); i++) {
            AccountData accountData = testAccounts.get(i);
            // Asignar usuario de forma circular
            User user = userList.get(i % userList.size());

            accountList.add(createAccount(
                    accountData.cbu,
                    accountData.alias,
                    accountData.availableBalance,
                    accountData.reservedBalance,
                    accountData.currency,
                    user
            ));
        }

        try {
            accountRepository.saveAll(accountList);
            logger.info("✅ {} cuentas creadas exitosamente", accountList.size());
        } catch (Exception e) {
            logger.error("Error al guardar cuentas: {}", e.getMessage(), e);
        }
    }

    private boolean checkIfDefaultAccountsExist() {
        // Verificar si alguna de las cuentas predeterminadas ya existe
        String[] defaultAliases = {
                "orange.cactus.wave",
                "silver.pixel.turbo",
                "velvet.shadow.coffee"
        };

        for (String alias : defaultAliases) {
            if (accountRepository.existsByAlias(alias)) {
                return true;
            }
        }
        return false;
    }

    private Account createAccount(String cbu, String alias, BigDecimal availableBalance,
                                  BigDecimal reservedBalance, CurrencyType currency, User user) {
        return Account.builder()
                .accountId(null)
                .cbu(cbu)
                .alias(alias)
                .availableBalance(availableBalance)
                .reservedBalance(reservedBalance)
                .currency(currency)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user)
                .reservations(new ArrayList<>())
                .movements(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build();
    }

    private String generateUniqueCBU(int index) {
        long timestampPart = System.currentTimeMillis() % 10000;
        int randomPart = secureRandom.nextInt(1000);
        return String.format("12312349%04d%03d%04d", index, randomPart, timestampPart);
    }

    // Clase auxiliar para datos de cuenta
    private static class AccountData {
        String cbu;
        String alias;
        BigDecimal availableBalance;
        BigDecimal reservedBalance;
        CurrencyType currency;

        AccountData(String cbu, String alias, BigDecimal availableBalance,
                    BigDecimal reservedBalance, CurrencyType currency) {
            this.cbu = cbu;
            this.alias = alias;
            this.availableBalance = availableBalance;
            this.reservedBalance = reservedBalance;
            this.currency = currency;
        }
    }
}