package com.wallex.financial_platform.service;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.wallex.financial_platform.dtos.responses.AccountResponseDTO;
import com.wallex.financial_platform.utils.SampleDataTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.services.impl.AccountService;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private SampleDataTest sampleDataTest = new SampleDataTest();

    private User sampleUser;
    private List<Account> sampleUserAccounts;


    @BeforeEach
    void setup(){
        sampleUser = sampleDataTest.getUserList().get(1);
        sampleUserAccounts = sampleDataTest.getAccountList().stream()
                .filter(account -> Objects.equals(account.getUser().getId(), sampleUser.getId()))
                .toList();
    }


    @Test
    void findAllAccountsByUser(){
        given(accountRepository.findById(any(Long.class)))
            .willReturn(Optional.ofNullable(sampleUserAccounts.getFirst()));

        AccountResponseDTO account = accountService.getAccountById(1L);

        assertThat(account.cbu()).isEqualTo(sampleUserAccounts.getFirst().getCbu());
    }

}
