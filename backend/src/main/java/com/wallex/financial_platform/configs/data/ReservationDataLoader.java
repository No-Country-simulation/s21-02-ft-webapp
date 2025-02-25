package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class ReservationDataLoader {

    private final ReservationRepository reservationRepository;
    private final AccountRepository accountRepository;

    public void load() {
        // Obtener algunas cuentas para asociar con las reservas
        Account account1 = accountRepository.findById(1L).orElseThrow();
        Account account2 = accountRepository.findById(2L).orElseThrow();

        // Crear las reservas para la cuenta 1
        Reservation reservation1 = new Reservation(
                null, // ID se genera automáticamente
                account1, // Relación con la cuenta 1
                new BigDecimal("500.00"), // Monto reservado
                LocalDateTime.now(), // Fecha de creación
                ReservationStatus.ACTIVE // Estado de la reserva
        );

        Reservation reservation2 = new Reservation(
                null,
                account1,
                new BigDecimal("1500.00"),
                LocalDateTime.now(),
                ReservationStatus.ACTIVE
        );

        // Crear las reservas para la cuenta 2
        Reservation reservation3 = new Reservation(
                null,
                account2,
                new BigDecimal("300.00"),
                LocalDateTime.now(),
                ReservationStatus.ACTIVE
        );

        Reservation reservation4 = new Reservation(
                null,
                account2,
                new BigDecimal("2000.00"),
                LocalDateTime.now(),
                ReservationStatus.CANCELED
        );

        // Guardar las reservas en el repositorio
        reservationRepository.saveAll(List.of(reservation1, reservation2, reservation3, reservation4)); // Guardar las reservas
    }
}
