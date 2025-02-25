package com.wallex.financial_platform.utils;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import com.wallex.financial_platform.entities.enums.CurrencyType;
import com.wallex.financial_platform.entities.enums.TransactionStatus;
import lombok.Getter;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.TransactionType;

import net.datafaker.Faker;


@Getter
public class SampleDataTest {

    private final Faker faker = new Faker();

    private final List<User> userList;
    private final List<Transaction> transactionsList;
    private final List<Account> accountList;

    public SampleDataTest() {
        userList = new ArrayList<>();
        accountList = new ArrayList<>();
        transactionsList = new ArrayList<>();
        for(int i=0;i < 20;i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            User user = User.builder()
                .id((long) (i + 1))
                .fullName(firstName+" "+lastName)
                .dni(faker.numerify("########"))
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.numerify("+54##########"))
                .password("qwertyui")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .accounts(new ArrayList<>())
                .notifications(new ArrayList<>())
                .cards(new ArrayList<>())
                .active(true)
                .build();
            userList.add(user);
            String accnumber = "000000000000".substring(0, 12-String.valueOf(accountList.size()+1).length())+(accountList.size()+1);
            Account account1 = Account.builder()
                .accountId(Long.valueOf(accountList.size()+1))
                .cbu(faker.numerify("000"+"0351"+"Ø"+accnumber+"Ø"))
                .alias(faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material())
                .currency(CurrencyType.ARS)
                .user(user)
                .availableBalance(BigDecimal.valueOf(0))
                .reservedBalance(BigDecimal.valueOf(0))
                .movements(new ArrayList<>())
                .reservations(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build();
            accountList.add(account1);
            String accnumber2 = "000000000000".substring(0, 12-String.valueOf(accountList.size()+1).length())+(accountList.size()+1);
            Account account2 = Account.builder()
                .accountId(Long.valueOf(accountList.size()+1))
                .cbu(faker.numerify("000"+"0351"+"Ø"+accnumber2+"Ø"))
                .alias(faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material())
                .currency(CurrencyType.USD)
                .user(user)
                .availableBalance(BigDecimal.valueOf(0))
                .reservedBalance(BigDecimal.valueOf(0))
                .movements(new ArrayList<>())
                .reservations(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .active(true)
                .build();
            accountList.add(account2);
            user.setAccounts(List.of(account1,account2));
        }
        for (int i = 0; i < 400; i++) {
            Account sourceAccount = accountList.get((int) (Math.random() * accountList.size()));
            Account destAccount = null;

            if (sourceAccount.getCurrency() == CurrencyType.ARS) {
                List<Account> ArsAccounts = accountList.stream()
                .filter(acc -> acc.getCurrency() == CurrencyType.ARS && !Objects.equals(acc.getAccountId(), sourceAccount.getAccountId()))
                .toList();
                destAccount = ArsAccounts.get((int) (Math.random() * ArsAccounts.size()));
            } else {
                List<Account> UsdAccounts = accountList.stream()
                .filter(acc -> Objects.equals(acc.getCurrency(), CurrencyType.USD) && !Objects.equals(acc.getAccountId(), sourceAccount.getAccountId()))
                .toList();
                destAccount = UsdAccounts.get((int) (Math.random() * UsdAccounts.size()));
            }
            Transaction transaction = Transaction.builder()
                .transactionId(Long.valueOf(transactionsList.size()+1))
                .sourceAccount(sourceAccount)
                .destinationAccount(destAccount)
                .amount(BigDecimal.valueOf(faker.number().randomDouble(2, 10000, 999999)))
                .type(TransactionType.TRANSFER)
                .reason("Buy appliance: "+faker.appliance().equipment())
                .transactionDateTime(LocalDateTime.ofInstant(
                                faker.timeAndDate().between(
                                        Instant.parse("2024-06-01T00:00:00Z"),
                                        Instant.parse("2025-02-28T00:00:00Z")
                                ), TimeZone.getDefault().toZoneId()))
                .status(TransactionStatus.values()[(int) (Math.random() * TransactionStatus.values().length)])
                .build();
            transactionsList.add(transaction);

            List<Transaction> sourceAccountSendedtransacions = sourceAccount.getSourceTransactions();
            sourceAccountSendedtransacions.add(transaction);
            sourceAccount.setSourceTransactions(sourceAccountSendedtransacions);
            List<Transaction> destAccountReceivedtransacions = destAccount.getDestinationTransactions();
            destAccountReceivedtransacions.add(transaction);
            destAccount.setDestinationTransactions(destAccountReceivedtransacions);

        }
    }
}
