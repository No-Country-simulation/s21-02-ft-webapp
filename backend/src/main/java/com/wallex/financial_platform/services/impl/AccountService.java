package com.wallex.financial_platform.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.wallex.financial_platform.dtos.requests.AccountRequestDto;
import com.wallex.financial_platform.dtos.responses.AccountResponseDTO;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.repositories.UserRepository;
import com.wallex.financial_platform.services.IAccountService;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.repositories.AccountRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountService implements IAccountService {
    private UserRepository userRepository;
    private AccountRepository accountRepository;

    @SneakyThrows
    public AccountResponseDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new RuntimeException("Account not found"));
        return mapToDTO(account);
    }
    @SneakyThrows
    public List<AccountResponseDTO> getByUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated() && auth.getPrincipal() == "anonymousUser") {
            throw new Exception("User not authenticated");
        }
        User user = userRepository.findByEmail(auth.getPrincipal().toString())
                .orElseThrow(() -> new RuntimeException("User not found"));
        List<Account> findUserAccounts = accountRepository.findByUserId(user.getId());
        return findUserAccounts.stream().map(account ->
            this.getAccountById(account.getAccountId())
        ).toList();
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
