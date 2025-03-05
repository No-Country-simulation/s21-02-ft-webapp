package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.CurrencyType;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountDataLoader {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public void load() {
        // Obtener algunos usuarios para asociar con las cuentas

        List<User> userList = userRepository.findAll();

        Faker faker = new Faker();

        List<Account> accountList = new ArrayList<>();

        // Crear las cuentas para el usuario 1
        accountList.add(
                Account.builder()
                .accountId(null)
                .cbu(faker.numerify("CBU000000"+"0351"+"1"+
                        "000000000000".substring(0, 12-String.valueOf(accountList.size()+1).length())+(accountList.size()+1)+
                        "1")
                ) // CBU único
                .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase()) // Alias único
                .availableBalance(new BigDecimal(10000000)) // Saldo disponible
                .reservedBalance(new BigDecimal(200)) // Saldo reservado
                .currency(CurrencyType.ARS) // Moneda
                .active(true)  // Activa
                .createdAt(LocalDateTime.now()) // Fecha de creación
                .updatedAt(LocalDateTime.now()) // Fecha de actualización
                .user(userList.get(0)) // Relación con el usuario 1
                // Puedes omitir o asignar null a los campos opcionales:
                .reservations(new ArrayList<>()) // Reservas (vacío si no tienes datos)
                .movements(new ArrayList<>())   // Movimientos (vacío si no tienes datos)
                .sourceTransactions(new ArrayList<>()) // Transacciones origen
                .destinationTransactions(new ArrayList<>()) // Transacciones destino
                .build()
        );


        accountList.add(
            Account.builder()
                .accountId(null)
                .cbu(faker.numerify("CBU000000"+"0351"+"1"+
                        "000000000000".substring(0, 12-String.valueOf(accountList.size()+1).length())+(accountList.size()+1)+
                        "1")
                ) // CBU único
                .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase())
                .availableBalance(new BigDecimal(250000))
                .reservedBalance(new BigDecimal(500))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userList.get(1))
                .reservations(new ArrayList<>())
                .movements(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build()
        );

        accountList.add(
            Account.builder()
                .accountId(null)
                .cbu(faker.numerify("CBU000000"+"0351"+"Ø"+
                        "000000000000".substring(0, 12-String.valueOf(accountList.size()+1).length())+(accountList.size()+1)+
                        "Ø")
                ) // CBU único
                .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase())
                .availableBalance(new BigDecimal(150000))
                .reservedBalance(new BigDecimal(300))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userList.get(2))
                .reservations(new ArrayList<>())
                .movements(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build()
        );

        accountList.add(
            Account.builder()
                .accountId(null)
                .cbu(faker.numerify("CBU000000"+"0351"+"Ø"+
                        "000000000000".substring(0, 12-String.valueOf(accountList.size()+1).length())+(accountList.size()+1)+
                        "Ø")
                ) // CBU único
                .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase())
                .availableBalance(new BigDecimal(500000))
                .reservedBalance(new BigDecimal(1000))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userList.get(3))
                .reservations(new ArrayList<>())
                .movements(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build()
        );

        accountList.add(
            Account.builder()
                .accountId(null)
                .cbu(faker.numerify("CBU000000"+"0351"+"Ø"+
                        "000000000000".substring(0, 12-String.valueOf(accountList.size()+1).length())+(accountList.size()+1)+
                        "Ø")
                ) // CBU único
                .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase())
                .availableBalance(new BigDecimal(350000))
                .reservedBalance(new BigDecimal(700))
                .currency(CurrencyType.ARS)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userList.get(4))
                .reservations(new ArrayList<>())
                .movements(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build()
        );

        accountList.add(
            Account.builder()
                .accountId(null)
                .cbu(faker.numerify("CBU000000"+"0351"+"Ø"+
                        "000000000000".substring(0, 12-String.valueOf(accountList.size()+1).length())+(accountList.size()+1)+
                        "Ø")
                ) // CBU único
                .alias((faker.animal().name()+"."+faker.construction().materials()+"."+faker.commerce().material()).toLowerCase())
                .availableBalance(new BigDecimal(1200))
                .reservedBalance(new BigDecimal(90))
                .currency(CurrencyType.USD)
                .active(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userList.get(0))
                .reservations(new ArrayList<>())
                .movements(new ArrayList<>())
                .sourceTransactions(new ArrayList<>())
                .destinationTransactions(new ArrayList<>())
                .build()
        );


        // Guardar las cuentas en el repositorio
        accountRepository.saveAll(accountList); // Guardar las cuentas
    }
}
