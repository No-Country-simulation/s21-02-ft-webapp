package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.CurrencyType;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountDataLoader {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public void load() {
        List<User> userList = userRepository.findAll();
        Faker faker = new Faker();
        List<Account> accountList = new ArrayList<>();

        accountList.add(
                Account.builder()
                .accountId(null)
                .cbu("1231234900000000000001")
                .alias("orange.cactus.wave") // Alias único
                .availableBalance(new BigDecimal(2500))
                .reservedBalance(new BigDecimal(0))
                .currency(CurrencyType.USD)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userList.get(0))
                .reservations(new ArrayList<>())
                .movements(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build()
        );


        accountList.add(
            Account.builder()
                .accountId(null)
                .cbu("1231234900000000000002")
                .alias("silver.pixel.turbo")
                .availableBalance(new BigDecimal(250000))
                .reservedBalance(new BigDecimal(500))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userList.get(0))
                .reservations(new ArrayList<>())
                .movements(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build()
        );

        accountList.add(
            Account.builder()
                .accountId(null)
                .cbu(faker.numerify("1231234900000000000003")
                ) // CBU único
                .alias("velvet.shadow.coffee")
                .availableBalance(new BigDecimal(150000))
                .reservedBalance(new BigDecimal(3000))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userList.get(1))
                .reservations(new ArrayList<>())
                .movements(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build()
        );

        accountList.add(
            Account.builder()
                .accountId(null)
                .cbu("1231234900000000000004")
                .alias("aqua.flame.breeze")
                .availableBalance(new BigDecimal(500000))
                .reservedBalance(new BigDecimal(1000))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userList.get(2))
                .reservations(new ArrayList<>())
                .movements(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build()
        );

        accountList.add(
            Account.builder()
                .accountId(null)
                .cbu("1231234900000000000005")
                .alias("crystal.echo.spark")
                .availableBalance(new BigDecimal(350000))
                .reservedBalance(new BigDecimal(700))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userList.get(3))
                .reservations(new ArrayList<>())
                .movements(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build()
        );

        accountList.add(
                Account.builder()
                        .accountId(null)
                        .cbu("1231234900000000000006")
                        .alias("rapid.fox.lake")
                        .availableBalance(new BigDecimal(350000))
                        .reservedBalance(new BigDecimal(700))
                        .currency(CurrencyType.ARS)
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .user(userList.get(5))
                        .reservations(new ArrayList<>())
                        .movements(new ArrayList<>())
                        .sourceTransactions(new ArrayList<>())
                        .destinationTransactions(new ArrayList<>())
                        .build()
        );

        accountRepository.saveAll(accountList);
    }
}
