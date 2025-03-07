package com.wallex.financial_platform.repositories;

import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByAccount_AccountId(Long accountId);
    List<Reservation> findByAccount_AccountIdAndStatus(Long accountId, ReservationStatus status);
}
