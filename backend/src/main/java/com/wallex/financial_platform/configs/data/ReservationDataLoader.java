package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.ReservationRepository;
import com.wallex.financial_platform.repositories.SuggestedReserveRepository;
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
    private final SuggestedReserveRepository reservationTypeRepository;

    public void load() {
        Account account1 = accountRepository.findById(2L).orElseThrow();
        Account account3 = accountRepository.findById(3L).orElseThrow();

        Reservation reservation1 = new Reservation(
                null,
                account1,
                new BigDecimal("50000.00"),
                "Vacaciones",
                LocalDateTime.now(),
                ReservationStatus.ACTIVE
        );

        Reservation reservation2 = new Reservation(
                null,
                account1,
                new BigDecimal("20000.00"),
                "Salud",
                LocalDateTime.now(),
                ReservationStatus.ACTIVE
        );

        Reservation reservation3 = new Reservation(
                null,
                account3,
                new BigDecimal("10000.00"),
                "Educación",
                LocalDateTime.now(),
                ReservationStatus.ACTIVE
        );

        reservationRepository.saveAll(List.of(reservation1, reservation2, reservation3));
    }
}
