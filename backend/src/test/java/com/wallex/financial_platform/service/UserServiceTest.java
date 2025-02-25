package com.wallex.financial_platform.service;

import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.repositories.UserRepository;
import com.wallex.financial_platform.services.impl.UserService;
import com.wallex.financial_platform.utils.SampleDataTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    private SampleDataTest sampleDataTest = new SampleDataTest();

    private List<User> sampleUsers;

    @BeforeEach
    void setup() {
        sampleUsers = sampleDataTest.getUserList();
    }

    @Test
    void findAllUsers() {
//        given(userRepository.findAll())
//            .willReturn(sampleUsers);

        //List<User> users = userService.getAllUsers();

        //assertThat(users).isEqualTo(sampleUsers);
    }
}
