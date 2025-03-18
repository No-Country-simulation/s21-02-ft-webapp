package com.wallex.financial_platform.services.utils;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.exceptions.account.AccountNotFoundException;
import com.wallex.financial_platform.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountContextService {
    private final AccountRepository accountRepository;
    public final static String  CBU_WALLEX = "CBU000000000000000000000000";

    public Account getAccountWallexPesos(){
        return this.accountRepository.findByCbuOrAlias(CBU_WALLEX,null).orElseThrow(() -> new AccountNotFoundException("Usuario no encontrado"));
    }
}
