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
        String USER_PASSWORD = "password123";
        String ENTITY_PASSWORD = "password456";

        LocalDateTime date1 = LocalDateTime.of(2025, 1, 2, 10, 0);
        LocalDateTime date2 = LocalDateTime.of(2025, 1, 5, 12, 30);
        LocalDateTime date3 = LocalDateTime.of(2025, 1, 10, 15, 45);
        LocalDateTime date4 = LocalDateTime.of(2025, 1, 15, 9, 15);
        LocalDateTime date5 = LocalDateTime.of(2025, 1, 20, 14, 0);
        LocalDateTime date6 = LocalDateTime.of(2025, 1, 25, 11, 30);

        User user1 = new User(
                null,
                "Delmer Rodríguez",
                "12345678",
                "jindrg@gmail.com",
                "+541112345678",
                passwordEncoder.encode(USER_PASSWORD),
                date1,
                date1,
                true,
                null,
                null,
                null
        );

        User user2 = new User(
                null,
                "Gustavo Paz",
                "87654321",
                "gusti.paz1@gmail.com",
                "+541198765432",
                passwordEncoder.encode(USER_PASSWORD),
                date2,
                date2,
                true,
                null,
                null,
                null
        );

        User user3 = new User(
                null,
                "Sebastián Tournier",
                "56789123",
                "sebastian.tournier1@gmail.com",
                "+541112345679",
                passwordEncoder.encode(USER_PASSWORD),
                date3,
                date3,
                true,
                null,
                null,
                null
        );

        User user4 = new User(
                null,
                "Gastón Federico Nahuel Gómez",
                "23456789",
                "gastongomez2014@hotmail.com",
                "+541112345680",
                passwordEncoder.encode(USER_PASSWORD),
                date4,
                date4,
                true,
                null,
                null,
                null
        );

        User user5 = new User(
                null,
                "Luis Méndez",
                "34567891",
                "luis.mendez@dominio.com",
                "+541112345681",
                passwordEncoder.encode(USER_PASSWORD),
                date5,
                date5,
                true,
                null,
                null,
                null
        );

        User user6 = new User(
                null,
                "Tesoreria Wallex",
                "71221356",
                "tesoreria@wallex.com",
                faker.numerify("+54##########"),
                passwordEncoder.encode(ENTITY_PASSWORD),
                date1,
                date1,
                false,
                null,
                null,
                null
        );

        userRepository.saveAll(List.of(user1, user2, user3, user4, user5, user6));
    }
}