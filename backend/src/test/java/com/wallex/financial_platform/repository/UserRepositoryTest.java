package com.wallex.financial_platform.repository;

import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.repositories.UserRepository;
import com.wallex.financial_platform.utils.SampleDataTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private static List<User> sampleUsers;

    @BeforeAll
    static void setup() {
        SampleDataTest sampleDataTest = new SampleDataTest();
        sampleUsers = sampleDataTest.getUserList();
    }

    @Test
    void testSaveUser() {
        User user = sampleUsers.get(0);
        user.setId(null);
        user.setAccounts(user.getAccounts().stream().map(account -> {
            account.setAccountId(null);
            account.setDestinationTransactions(new ArrayList<>());
            account.setSourceTransactions(new ArrayList<>());
            return account;
        }).toList());
        User savedUser = userRepository.save(user);
        assertThat(savedUser).isEqualTo(user);

    }
}
