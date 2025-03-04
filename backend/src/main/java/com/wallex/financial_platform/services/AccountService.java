package com.wallex.financial_platform.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.repository.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService {
    private AccountRepository accountRepository;
    
    public List<Account> getAllWallets() {
        return accountRepository.findAll();
    }
}
