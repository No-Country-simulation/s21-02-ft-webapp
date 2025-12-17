package com.wallex.financial_platform.repositories;

import com.wallex.financial_platform.entities.enums.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wallex.financial_platform.entities.Account;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> { ;
    List<Account> findByUserId(Long userId);
    Optional<Account> findByCbuOrAlias(String cbu, String alias);
    List<Account> findByCurrency(CurrencyType attr0);
    Optional<Account> findByAlias(String alias);
    List<Account> findFirst2ByOrderByCreatedAtAsc();
    List<Account> findFirst5ByOrderByCreatedAtAsc();
    boolean existsByAlias(String alias);
}
