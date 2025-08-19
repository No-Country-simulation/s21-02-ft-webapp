package com.wallex.financial_platform.repositories;

import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.SuggestedReserve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SuggestedReserveRepository extends JpaRepository<SuggestedReserve, Long> {
    boolean existsByName(String name);
    Optional<SuggestedReserve> findByName(String name);
}
