package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.ReservationType;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.ReservationRepository;
import com.wallex.financial_platform.repositories.ReservationTypeRepository;
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
    private final ReservationTypeRepository reservationTypeRepository;

    public void load() {
        Account account1 = accountRepository.findById(2L).orElseThrow();
        Account account3 = accountRepository.findById(3L).orElseThrow();

        // Cargar ReservationType existentes
        ReservationType comidaType = reservationTypeRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("ReservationType 'Comida' no encontrado"));
        ReservationType educacionType = reservationTypeRepository.findById(2L)
                .orElseThrow(() -> new RuntimeException("ReservationType 'Educacion' no encontrado"));
        ReservationType vacacionesType = reservationTypeRepository.findById(3L)
                .orElseThrow(() -> new RuntimeException("ReservationType 'Vacaciones Familiares' no encontrado"));

        Reservation reservation1 = new Reservation(
                null,
                account1,
                new BigDecimal("50000.00"),
                LocalDateTime.now(),
                ReservationStatus.ACTIVE,
                comidaType
        );

        Reservation reservation2 = new Reservation(
                null,
                account1,
                new BigDecimal("20000.00"),
                LocalDateTime.now(),
                ReservationStatus.ACTIVE,
                educacionType
        );

        Reservation reservation3 = new Reservation(
                null,
                account3,
                new BigDecimal("10000.00"),
                LocalDateTime.now(),
                ReservationStatus.ACTIVE,
                vacacionesType
        );

        reservationRepository.saveAll(List.of(reservation1, reservation2, reservation3));
    }
}
