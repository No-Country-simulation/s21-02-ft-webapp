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
                .cbu("CBU000000000000000000000001")
                .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase()) // Alias único
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
                .cbu("CBU000000000000000000000002")
                .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase())
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
                .cbu(faker.numerify("CBU000000000000000000000003")
                ) // CBU único
                .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase())
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
                .cbu("CBU000000000000000000000004")
                .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase())
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
                .cbu("CBU000000000000000000000005")
                .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase())
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
                        .cbu("CBU000000000000000000000000")
                        .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase())
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
