package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
import com.wallex.financial_platform.entities.enums.TypeReservation;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReservationDataLoader {

    private final ReservationRepository reservationRepository;
    private final AccountRepository accountRepository;

    public void load() {
        Account account1 = accountRepository.findById(1L).orElseThrow();
        Account account3 = accountRepository.findById(3L).orElseThrow();

        Reservation reservation1 = new Reservation(
                null,
                account1,
                new BigDecimal("500.00"),
                LocalDateTime.now(),
                ReservationStatus.ACTIVE,
                TypeReservation.COMIDA
        );

        Reservation reservation2 = new Reservation(
                null,
                account3,
                new BigDecimal("100.00"),
                LocalDateTime.now(),
                ReservationStatus.ACTIVE,
                TypeReservation.VACACIONES_FAMILIARES
        );

        reservationRepository.saveAll(List.of(reservation1, reservation2));
    }
}
