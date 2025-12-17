package com.wallex.financial_platform.repositories;

import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByAccount_AccountId(Long accountId);
    List<Reservation> findByAccount_AccountIdAndStatus(Long accountId, ReservationStatus status);
    Optional<Reservation> findByAccount_AccountIdAndReasonAndStatus(Long accountId, String reason, ReservationStatus status);

}

