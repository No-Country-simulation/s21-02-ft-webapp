package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.CurrencyType;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class AccountDataLoader {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public void load() {
        // Obtener algunos usuarios para asociar con las cuentas
        User user1 = userRepository.findById(1L).orElseThrow();
        User user2 = userRepository.findById(2L).orElseThrow();
        User user3 = userRepository.findById(3L).orElseThrow();
        User user4 = userRepository.findById(4L).orElseThrow();
        User user5 = userRepository.findById(5L).orElseThrow();

        // Crear las cuentas para el usuario 1
        Account account1 = Account.builder()
                .accountId(null) // ID se genera automáticamente
                .cbu("CBU123456789012345678901234") // CBU único
                .alias("Alias1") // Alias único
                .availableBalance(new BigDecimal(1000)) // Saldo disponible
                .reservedBalance(new BigDecimal(200)) // Saldo reservado
                .currency(CurrencyType.ARS) // Moneda
                .active(true)  // Activa
                .createdAt(LocalDateTime.now()) // Fecha de creación
                .updatedAt(LocalDateTime.now()) // Fecha de actualización
                .user(user1) // Relación con el usuario 1
                // Puedes omitir o asignar null a los campos opcionales:
                .reservations(null) // Reservas (vacío si no tienes datos)
                .movements(null)   // Movimientos (vacío si no tienes datos)
                .sourceTransactions(new ArrayList<>()) // Transacciones origen
                .destinationTransactions(new ArrayList<>()) // Transacciones destino
                .build();

        Account account3 = Account.builder()
                .accountId(null)
                .cbu("CBU111111111111111111111111")
                .alias("Alias3")
                .availableBalance(new BigDecimal(2500))
                .reservedBalance(new BigDecimal(500))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user2)
                .reservations(null)
                .movements(null)
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build();

        Account account4 = Account.builder()
                .accountId(null)
                .cbu("CBU222222222222222222222222")
                .alias("Alias4")
                .availableBalance(new BigDecimal(1500))
                .reservedBalance(new BigDecimal(300))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user3)
                .reservations(null)
                .movements(null)
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build();

        Account account2 = Account.builder()
                .accountId(null)
                .cbu("CBU987654321098765432109876")
                .alias("Alias2")
                .availableBalance(new BigDecimal(5000))
                .reservedBalance(new BigDecimal(1000))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user4)
                .reservations(null)
                .movements(null)
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build();

        Account account5 = Account.builder()
                .accountId(null)
                .cbu("CBU333333333333333333333333")
                .alias("Alias5")
                .availableBalance(new BigDecimal(3500))
                .reservedBalance(new BigDecimal(700))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user5)
                .reservations(null)
                .movements(null)
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build();

        Account account6 = Account.builder()
                .accountId(null)
                .cbu("CBU444444444444444444444444")
                .alias("Alias6")
                .availableBalance(new BigDecimal(4500))
                .reservedBalance(new BigDecimal(900))
                .currency(CurrencyType.USD)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(user5)
                .reservations(null)
                .movements(null)
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build();


        // Guardar las cuentas en el repositorio
        accountRepository.saveAll(List.of(account1, account2, account3, account4, account5, account6)); // Guardar las cuentas
    }
}
