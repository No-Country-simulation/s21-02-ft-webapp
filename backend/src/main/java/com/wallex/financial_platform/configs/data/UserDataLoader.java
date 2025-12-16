package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDataLoader {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void load() {
        // Datos de usuarios de prueba
        List<UserData> testUsers = List.of(
                new UserData("Delmer Rodríguez", "12345678", "jindrg@gmail.com", "password123", true),
                new UserData("Gustavo Paz", "87654321", "gusti.paz11@gmail.com", "password123", true),
                new UserData("Sebastián Tournier", "56789123", "sebastian.tournier11@gmail.com", "password123", true),
                new UserData("Gastón Federico Nahuel Gómez", "23456789", "gastongomez2014@hotmail.com", "password123", true),
                new UserData("Luis Méndez", "34567891", "luis.mendez@dominio.com", "password123", true),
                new UserData("Tesoreria Wallex", "71221356", "tesoreria@wallex.com", "password456", false)
        );

        List<User> usersToCreate = new ArrayList<>();
        int created = 0;
        int skipped = 0;

        for (UserData userData : testUsers) {
            // Verificar si el usuario ya existe por DNI o email
            boolean existsByDni = userRepository.existsByDni(userData.dni);
            boolean existsByEmail = userRepository.existsByEmail(userData.email);

            if (!existsByDni && !existsByEmail) {
                usersToCreate.add(buildUser(userData));
                created++;
            } else {
                skipped++;
            }
        }

        if (!usersToCreate.isEmpty()) {
            userRepository.saveAll(usersToCreate);
            System.out.println("✅ Usuarios creados: " + created + " | Omitidos: " + skipped);
        } else {
            System.out.println("📝 Todos los usuarios ya existen en la base de datos");
        }
    }

    private User buildUser(UserData userData) {
        LocalDateTime now = LocalDateTime.now();

        return new User(
                null,
                userData.fullName,
                userData.dni,
                userData.email,
                generatePhoneNumber(),
                passwordEncoder.encode(userData.rawPassword),
                now,
                now,
                userData.isPerson,
                null,
                null,
                null
        );
    }

    private String generatePhoneNumber() {
        long base = 1000000000L;
        long random = (long) (Math.random() * 9000000000L);
        return "+549" + (base + random);
    }

    // Clase auxiliar para datos de usuario
    private static class UserData {
        String fullName;
        String dni;
        String email;
        String rawPassword;
        boolean isPerson;

        UserData(String fullName, String dni, String email,
                 String rawPassword, boolean isPerson) {
            this.fullName = fullName;
            this.dni = dni;
            this.email = email;
            this.rawPassword = rawPassword;
            this.isPerson = isPerson;
        }
    }
}