package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDataLoader {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void load() {
        Faker faker = new Faker();

        // Fechas hardcodeadas en enero de 2025
        LocalDateTime date1 = LocalDateTime.of(2025, 1, 2, 10, 0);
        LocalDateTime date2 = LocalDateTime.of(2025, 1, 5, 12, 30);
        LocalDateTime date3 = LocalDateTime.of(2025, 1, 10, 15, 45);
        LocalDateTime date4 = LocalDateTime.of(2025, 1, 15, 9, 15);
        LocalDateTime date5 = LocalDateTime.of(2025, 1, 20, 14, 0);
        LocalDateTime date6 = LocalDateTime.of(2025, 1, 25, 11, 30);

        // Crear usuarios con contraseñas encriptadas
        User user1 = new User(
                null,
                "Delmer Rodríguez",
                "12345678",
                "jindrg@gmail.com",
                "+541112345678",
                passwordEncoder.encode("password123"),
                date1,
                date1,
                true,
                null,
                null,
                null,
                null,
                false
        );

        User user2 = new User(
                null,
                "Gustavo Paz",
                "87654321",
                "gusti.paz@gmail.com",
                "+541198765432",
                passwordEncoder.encode("password456"),
                date2,
                date2,
                true,
                null,
                null,
                null,
                null,
                false
        );

        User user3 = new User(
                null,
                "Sebastián Tournier",
                "56789123",
                "sebastian.tournier@gmail.com",
                "+541112345679",
                passwordEncoder.encode("password789"),
                date3,
                date3,
                true,
                null,
                null,
                null,
                null,
                false
        );

        User user4 = new User(
                null,
                "Gastón Federico Nahuel Gómez",
                "23456789",
                "gastongomez2014@hotmail.com",
                "+541112345680",
                passwordEncoder.encode("password012"),
                date4,
                date4,
                true,
                null,
                null,
                null,
                null,
                false
        );

        User user5 = new User(
                null,
                "Luis Méndez",
                "34567891",
                "luis.mendez@dominio.com",
                "+541112345681",
                passwordEncoder.encode("password345"),
                date5,
                date5,
                true,
                null,
                null,
                null,
                null,
                false
        );

        User user6 = new User(
                null,
                "MERCADO PAGO SERVICIOS DE PROCESAMIENTO S.R.L",
                "71699949",
                "support@mercadopago.com",
                faker.numerify("+54##########"),
                passwordEncoder.encode("password345"),
                date6,
                date6,
                false,
                null,
                null,
                null,
                null,
                false
        );

        User user7 = new User(
                null,
                "Tesoreria Wallex",
                "71221356",
                "tesoreria@wallex.com",
                faker.numerify("+54##########"),
                passwordEncoder.encode("qwertyui"),
                date1,
                date1,
                false,
                null,
                null,
                null,
                null,
                false
        );

        User user8 = new User(
                null,
                "Visa",
                "71135628",
                "visa@testing.com",
                faker.numerify("+54##########"),
                passwordEncoder.encode("qwertyui"),
                date2,
                date2,
                false,
                null,
                null,
                null,
                null,
                true
        );

        User user9 = new User(
                null,
                "MasterCard",
                "71003569",
                "mastercard@testing.com",
                faker.numerify("+54##########"),
                passwordEncoder.encode("qwertyui"),
                date3,
                date3,
                false,
                null,
                null,
                null,
                null,
                true
        );

        User user10 = new User(
                null,
                "American Express",
                "73100620",
                "amex@testing.com",
                faker.numerify("+54##########"),
                passwordEncoder.encode("qwertyui"),
                date4,
                date4,
                false,
                null,
                null,
                null,
                null,
                true
        );

        userRepository.saveAll(List.of(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10));
    }
}