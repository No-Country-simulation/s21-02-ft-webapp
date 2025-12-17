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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ReservationDataLoader {

    private final ReservationRepository reservationRepository;
    private final AccountRepository accountRepository;

    public void load() {
        // Buscar cuentas por CBU/alias en lugar de ID fijo
        List<Account> accounts = findAccountsForReservations();

        if (accounts.isEmpty()) {
            System.out.println("⚠️ No se encontraron cuentas para crear reservas");
            return;
        }

        List<Reservation> reservations = new ArrayList<>();

        // Crear reservas para las primeras cuentas encontradas
        reservations.add(createReservation(
                accounts.get(0),
                new BigDecimal("50000.00"),
                "Vacaciones",
                ReservationStatus.ACTIVE
        ));

        reservations.add(createReservation(
                accounts.get(0),
                new BigDecimal("20000.00"),
                "Salud",
                ReservationStatus.ACTIVE
        ));

        if (accounts.size() >= 2) {
            reservations.add(createReservation(
                    accounts.get(1),
                    new BigDecimal("10000.00"),
                    "Educación",
                    ReservationStatus.ACTIVE
            ));
        }

        reservationRepository.saveAll(reservations);
        System.out.println("✅ " + reservations.size() + " reservas creadas");
    }

    private List<Account> findAccountsForReservations() {
        List<Account> accounts = new ArrayList<>();

        // Buscar cuentas por CBU o alias en lugar de ID
        Optional<Account> account1 = accountRepository.findByCbuOrAlias("1231234900000000000002", "silver.pixel.turbo");
        Optional<Account> account2 = accountRepository.findByCbuOrAlias(generateSimpleCBU(3), "velvet.shadow.coffee");

        account1.ifPresent(accounts::add);
        account2.ifPresent(accounts::add);

        // Si no se encuentran por CBU/alias, tomar las primeras cuentas
        if (accounts.isEmpty()) {
            accounts = accountRepository.findFirst2ByOrderByCreatedAtAsc();
        }

        return accounts;
    }

    private String generateSimpleCBU(int accountNumber) {
        return String.format("12312349%012d", accountNumber);
    }

    private Reservation createReservation(Account account, BigDecimal amount,
                                          String description, ReservationStatus status) {
        return new Reservation(
                null,
                account,
                amount,
                description,
                LocalDateTime.now(),
                status
        );
    }
}