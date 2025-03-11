package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
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
        Account account2 = accountRepository.findById(2L).orElseThrow();

        LocalDateTime date1 = LocalDateTime.of(2025, 3, 5, 12, 0);
        LocalDateTime date2 = LocalDateTime.of(2025, 3, 6, 15, 45);
        LocalDateTime date3 = LocalDateTime.of(2025, 3, 10, 9, 30);


        Reservation reservation1 = new Reservation(
                null,
                account1,
                new BigDecimal("10000.00"),
                date1,
                ReservationStatus.ACTIVE,
                "para Abono celular"
        );

        Reservation reservation2 = new Reservation(
                null,
                account1,
                new BigDecimal("70000.00"),
                date2,
                ReservationStatus.ACTIVE,
                "para facultad"
        );

        Reservation reservation3 = new Reservation(
                null,
                account2,
                new BigDecimal("30000.00"),
                date3,
                ReservationStatus.ACTIVE,
                "para salud"
        );

        reservationRepository.saveAll(List.of(reservation1, reservation2, reservation3));
    }
}