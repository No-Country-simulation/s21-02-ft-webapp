package com.wallex.financial_platform.repositories;

import com.wallex.financial_platform.entities.ReservationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationTypeRepository extends JpaRepository<ReservationType, Long> {
    boolean existsByName(String name);
}
