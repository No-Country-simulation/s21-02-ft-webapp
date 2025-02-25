package com.wallex.financial_platform.repository;


import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.TransactionRepository;
import com.wallex.financial_platform.repositories.UserRepository;
import com.wallex.financial_platform.utils.SampleDataTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class AccountRepositoryTest {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    private SampleDataTest sampleDataTest = new SampleDataTest();

    private List<Account> accountsList;
    private Account sampleAccount1;
    private Account sampleAccount2;
    private User sampleUser;

    @BeforeEach
    void setup(){
        accountsList = sampleDataTest.getAccountList();
        sampleAccount1 = accountsList.get(0);
        sampleAccount1.setSourceTransactions(new ArrayList<>());
        sampleAccount1.setDestinationTransactions(new ArrayList<>());
        User newUser = sampleAccount1.getUser();
        newUser.setId(null);
        newUser.setAccounts(new ArrayList<>());
        sampleUser = userRepository.save(newUser);
    }

    @Test
    void testSaveAccount() {
        sampleAccount1.setAccountId(null);
        sampleAccount1.setUser(sampleUser);

        Account savedAccount = accountRepository.save(sampleAccount1);
        Account findAccount = accountRepository.findById(sampleAccount1.getAccountId()).orElse(null);

        assertEquals(savedAccount, findAccount);
    }

}
