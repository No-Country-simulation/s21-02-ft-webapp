package com.wallex.financial_platform.repositories;

import com.wallex.financial_platform.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.wallex.financial_platform.entities.Transaction;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountAccountIdOrDestinationAccountAccountId(Long accountId, Long accountId1);

    @Query("SELECT DISTINCT t FROM Transaction t " +
            "LEFT JOIN FETCH t.sourceAccount " +
            "LEFT JOIN FETCH t.destinationAccount")
    List<Transaction> findAllWithAccounts();
}
