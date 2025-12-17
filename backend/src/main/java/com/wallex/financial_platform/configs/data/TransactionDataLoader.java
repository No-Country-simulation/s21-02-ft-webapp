package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.enums.TransactionStatus;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionDataLoader {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public void load() {
        List<Account> accounts = findAccountsForTransactions();

        if (accounts.size() < 2) {
            System.out.println("⚠️ No hay suficientes cuentas para crear transacciones");
            return;
        }

        List<Transaction> transactions = new ArrayList<>();

        if (accounts.size() >= 3) {
            transactions.add(new Transaction(
                    null,
                    accounts.get(1),
                    accounts.get(2),
                    new BigDecimal("150000.00").negate(),
                    TransactionType.TRANSFER,
                    "Pago de alquiler",
                    LocalDateTime.now(),
                    TransactionStatus.COMPLETED,
                    new ArrayList<>()
            ));
        }

        if (accounts.size() >= 4) {
            transactions.add(new Transaction(
                    null,
                    accounts.get(1),
                    accounts.get(3),
                    new BigDecimal("100000.00").negate(),
                    TransactionType.TRANSFER,
                    "Pago por préstamo",
                    LocalDateTime.now(),
                    TransactionStatus.COMPLETED,
                    new ArrayList<>()
            ));
        }

        if (accounts.size() >= 2) {
            transactions.add(new Transaction(
                    null,
                    accounts.get(1),
                    accounts.get(1),
                    new BigDecimal("20000.00"),
                    TransactionType.DEPOSIT,
                    "Ingreso de dinero desde tarjeta de débito",
                    LocalDateTime.now(),
                    TransactionStatus.COMPLETED,
                    new ArrayList<>()
            ));
        }

        if (!transactions.isEmpty()) {
            transactionRepository.saveAll(transactions);
            System.out.println("✅ " + transactions.size() + " transacciones creadas");
        }
    }

    private List<Account> findAccountsForTransactions() {
        List<Account> accounts = new ArrayList<>();

        String[] aliases = {
                "orange.cactus.wave",
                "silver.pixel.turbo",
                "velvet.shadow.coffee",
                "aqua.flame.breeze",
                "crystal.echo.spark"
        };

        for (String alias : aliases) {
            accountRepository.findByAlias(alias)
                    .ifPresent(accounts::add);
        }

        if (accounts.size() < 4) {
            List<Account> firstAccounts = accountRepository.findFirst5ByOrderByCreatedAtAsc();
            firstAccounts.forEach(account -> {
                if (!accounts.contains(account)) {
                    accounts.add(account);
                }
            });
        }

        return accounts;
    }
}