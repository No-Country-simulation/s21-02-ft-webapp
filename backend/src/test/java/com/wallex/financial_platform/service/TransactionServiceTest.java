package com.wallex.financial_platform.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.dtos.responses.UserResponseDTO;
import com.wallex.financial_platform.repositories.UserRepository;
import com.wallex.financial_platform.services.impl.UserService;
import com.wallex.financial_platform.services.utils.UserContextService;
import com.wallex.financial_platform.utils.SampleDataTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Transaction;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.repositories.TransactionRepository;
import com.wallex.financial_platform.services.impl.TransactionService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserContextService userContextService;

    @InjectMocks
    private TransactionService transactionService;

    private SampleDataTest sampleDataTest = new SampleDataTest();

    private User sampleUser1;
    private User sampleUser2;
    private List<Account> sampleUser1Accounts;
    private List<Account> sampleUser2Accounts;
    private List<Transaction> sampleTransacctions;

    @BeforeEach
    void setup(){
       /* sampleUser1 = sampleDataTest.getUserList().get(0);
        sampleUser2 = sampleDataTest.getUserList().get(1);
        sampleUser1Accounts = sampleDataTest.getAccountList().subList(0, 2);
        sampleUser2Accounts = sampleDataTest.getAccountList().subList(2, 3);
        sampleTransacctions = sampleDataTest.getTransactionsList();*/

        when(userContextService.getAuthenticatedUser())
                .thenReturn(sampleUser1);
    }

/*    @Test
    @DisplayName("Test find all transactions")
    void findAllTransaction(){
        Transaction transaction = sampleTransacctions.getFirst();

        given(transactionRepository.findByTransactionIdAndUser(transaction.getTransactionId(), sampleUser1.getId()))
                .willReturn(Optional.of(transaction));

        TransactionResponseDTO transactionResponse = transactionService.getById(sampleTransacctions.getFirst().getTransactionId());

        assertThat(transactionResponse).isNotNull();
        assertThat(transactionResponse.amount()).isEqualTo(sampleTransacctions.getFirst().getAmount());
    }*/

}
