package com.wallex.financial_platform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wallex.financial_platform.dtos.responses.AccountResponseDTO;
import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.services.impl.AccountService;
import com.wallex.financial_platform.utils.SampleDataTest;

import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockitoBean
    AccountService accountService;

    SampleDataTest sampleDataTest = new SampleDataTest();

    List<Account> accountsList;

    List<AccountResponseDTO> accountResponseDTOList;

    @BeforeEach
    void setup(){
        User user = sampleDataTest.getUserList().getFirst();
        accountsList = user.getAccounts();
        accountResponseDTOList = accountsList.stream().map(this::mapToDTO).toList();
        assertThat(accountsList).isNotEmpty();
        assertThat(accountsList).hasSize(2);

        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication auth = Mockito.mock(Authentication.class);
        when(securityContext.getAuthentication())
                .thenReturn(auth);
        when(auth.getPrincipal())
                .thenReturn(user.getEmail());
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @DisplayName("Test find all accounts By User")
    void testFindAllAccountsByUser() throws Exception {
        given(accountService.getByUser())
                .willReturn(accountResponseDTOList);

        this.mockMvc.perform(get("/api/accounts").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", Matchers.hasSize(accountResponseDTOList.size())))
                .andExpect(jsonPath("$[0].cbu").value(accountResponseDTOList.get(0).cbu()))
                .andExpect(jsonPath("$[0].alias").value(accountResponseDTOList.get(0).alias()))
                .andExpect(jsonPath("$[0].currency").value(accountResponseDTOList.get(0).currency().toString()))
                .andExpect(jsonPath("$[0].balance").value(accountResponseDTOList.get(0).balance()))
        ;
    }

    private AccountResponseDTO mapToDTO(Account account) {
        return new AccountResponseDTO(
                account.getCbu(),
                account.getAlias(),
                account.getCurrency(),
                account.getAvailableBalance()
        );
    }
}
