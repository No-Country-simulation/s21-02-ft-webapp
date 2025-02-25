package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.repositories.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class UserDataLoader {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    // Constructor manual
    public UserDataLoader(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void load() {
        // Crear usuarios con contraseñas encriptadas
        User user1 = new User(
                null, // ID generado automáticamente
                "Juan Pérez", // Nombre completo
                "12345678", // DNI
                "juan.perez@dominio.com", // Email
                "+541112345678", // Teléfono
                passwordEncoder.encode("password123"), // Contraseña encriptada
                LocalDateTime.now(), // Fecha de creación
                LocalDateTime.now(), // Fecha de actualización
                true, // Estado activo
                null, // Listado de cuentas
                null, // Listado de notificaciones
                null  // Listado de tarjetas
        );

        User user2 = new User(
                null,
                "María Gómez",
                "87654321",
                "maria.gomez@dominio.com",
                "+541198765432",
                passwordEncoder.encode("password456"), // Contraseña encriptada
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                null,
                null,
                null
        );

        User user3 = new User(
                null,
                "Carlos López",
                "56789123",
                "carlos.lopez@dominio.com",
                "+541112345679",
                passwordEncoder.encode("password789"), // Contraseña encriptada
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                null,
                null,
                null
        );

        User user4 = new User(
                null,
                "Ana Torres",
                "23456789",
                "ana.torres@dominio.com",
                "+541112345680",
                passwordEncoder.encode("password012"), // Contraseña encriptada
                LocalDateTime.now(),
                LocalDateTime.now(),
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
                passwordEncoder.encode("password345"), // Contraseña encriptada
                LocalDateTime.now(),
                LocalDateTime.now(),
                true,
                null,
                null,
                null
        );

        // Guardar los usuarios en la base de datos
        userRepository.saveAll(List.of(user1, user2, user3, user4, user5));
    }

}
