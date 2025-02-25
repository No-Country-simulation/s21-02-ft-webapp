package com.wallex.financial_platform.repository;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.TransactionStatus;
import com.wallex.financial_platform.entities.enums.TransactionType;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import com.wallex.financial_platform.repositories.UserRepository;
import com.wallex.financial_platform.utils.SampleDataTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class TransactionRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    private SampleDataTest sampleDataTest = new SampleDataTest();

    private List<Account> sampleCounterPartAccounts = new ArrayList<>();
    private Account sampleAccount1;
    private Account sampleAccount2;
    private User sampleUser;

    @BeforeEach
    void setup(){
        sampleAccount1 = sampleDataTest.getAccountList().get(0);
        List<Account> counterPartAccounts = List.of(
                sampleAccount1.getDestinationTransactions().stream()
                    .map(Transaction::getSourceAccount)
                    .toList(),
                sampleAccount1.getSourceTransactions().stream()
                        .map(Transaction::getDestinationAccount)
                        .toList()
                ).stream()
                .flatMap(List::stream)
                .toList();
        counterPartAccounts.stream().forEach(account -> {
            Optional<User> foundUser = userRepository.findByDni(account.getUser().getDni());
            if (!foundUser.isPresent()) {
                User createUser = account.getUser();
                createUser.setId(null);
                createUser.setAccounts(new ArrayList<>());
                foundUser = Optional.of(userRepository.save(account.getUser()));
            }
            account.setUser(foundUser.get());
            Optional<Account> foundAccount = accountRepository.findByCbu(account.getCbu());
            if (!foundAccount.isPresent()) {
                account.setAccountId(null);
                account.setSourceTransactions(new ArrayList<>());
                account.setDestinationTransactions(new ArrayList<>());
                foundAccount = Optional.of(accountRepository.save(account));
            }
            account.setAccountId(foundAccount.get().getAccountId());
            if (!sampleCounterPartAccounts.contains(account)) {
                sampleCounterPartAccounts.add(account);
            }
        });

        sampleAccount1.setSourceTransactions(new ArrayList<>());
        sampleAccount1.setDestinationTransactions(new ArrayList<>());
        User newUser = sampleAccount1.getUser();
        newUser.setId(null);
        newUser.setAccounts(new ArrayList<>());
        sampleUser = userRepository.save(newUser);

    }

    @Test
    @DisplayName("Test save transactions, update account balance")
    void testSaveTransactions() {
        sampleAccount1.setAccountId(null);
        sampleAccount1.setUser(sampleUser);

        Account savedAccount1 = accountRepository.save(sampleAccount1);
        Account findAccount1 = accountRepository.findById(sampleAccount1.getAccountId()).orElse(null);
        assertEquals(savedAccount1, findAccount1);

        List<Transaction> sampleTransactions = Stream.of(
                sampleDataTest.getAccountList().get(0).getSourceTransactions().stream()
                        .map(transaction -> {
                            transaction.setTransactionId(null);
                            transaction.setSourceAccount(savedAccount1);
                            transaction.setDestinationAccount(
                                    sampleCounterPartAccounts.stream()
                                            .filter(account -> account.getCbu().equals(transaction.getDestinationAccount().getCbu()))
                                            .findFirst().get()
                            );
                            return transaction;
                        }).toList(),
                sampleDataTest.getAccountList().get(0).getDestinationTransactions().stream()
                        .map(transaction -> {
                            transaction.setTransactionId(null);
                            transaction.setDestinationAccount(savedAccount1);
                            transaction.setSourceAccount(
                                    sampleCounterPartAccounts.stream()
                                            .filter(account -> account.getCbu().equals(transaction.getSourceAccount().getCbu()))
                                            .findFirst().get()
                            );
                            return transaction;
                        }).toList()
        )
        .flatMap(List::stream)
        .toList();

        sampleTransactions.stream().forEach(transaction -> {
            Transaction savedTransaction = transactionRepository.save(transaction);
            Transaction findTransaction = transactionRepository.findById(savedTransaction.getTransactionId()).orElse(null);
            assertEquals(savedTransaction, findTransaction);
        });

        Account updatedAccount = accountRepository.findById(savedAccount1.getAccountId()).orElse(null);
        System.out.println(updatedAccount.toString());

        Map<TransactionType, Map<TransactionStatus, BigDecimal>> sampleBalance = calculateBalance(sampleAccount1);

        assertEquals(
                sampleBalance.get(TransactionType.TRANSFER).get(TransactionStatus.COMPLETED),
                updatedAccount.getTransactionTypeBalances().get(TransactionType.TRANSFER).get(TransactionStatus.COMPLETED)
        );

    }

    private Map<TransactionType, Map<TransactionStatus, BigDecimal>> calculateBalance(Account account) {
        Map<TransactionType, Map<TransactionStatus, BigDecimal>> balances = generateMapping();

        if (!account.getDestinationTransactions().isEmpty()) {
            account.getDestinationTransactions().stream()
                    .forEach(transaction -> {
                        System.out.println(balances.toString());
                        Map<TransactionStatus, BigDecimal> currentBalanceType = balances.get(transaction.getType());
                        currentBalanceType.put(
                                transaction.getStatus(),
                                currentBalanceType.get(transaction.getStatus()).add(transaction.getAmount())
                        );
                        balances.put(
                                transaction.getType(),
                                currentBalanceType
                        );
                    });
        }
        if (!account.getSourceTransactions().isEmpty()) {
            account.getSourceTransactions().stream()
                    .forEach(transaction -> {
                        Map<TransactionStatus, BigDecimal> currentBalanceType = balances.get(transaction.getType());
                        currentBalanceType.put(
                                transaction.getStatus(),
                                currentBalanceType.get(transaction.getStatus()).subtract(transaction.getAmount())
                        );
                        balances.put(
                                transaction.getType(),
                                currentBalanceType
                        );
                    });
        }
        return balances;
    }

    private Map<TransactionType, Map<TransactionStatus, BigDecimal>>  generateMapping() {
        Map<TransactionType, Map<TransactionStatus, BigDecimal>> mapping = new HashMap<>();
        for (TransactionType transactionType : TransactionType.values()) {
            Map<TransactionStatus, BigDecimal> balanceType = new HashMap<>();
            for (TransactionStatus transactionStatus : TransactionStatus.values()) {
                balanceType.put(transactionStatus, BigDecimal.ZERO);
            }
            mapping.put(transactionType, balanceType);
        }
        return mapping;
    }
}
