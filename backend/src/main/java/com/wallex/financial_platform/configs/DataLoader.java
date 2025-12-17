package com.wallex.financial_platform.configs;

import com.wallex.financial_platform.configs.data.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final Environment environment;

    @Bean
    public CommandLineRunner loadData(
            UserDataLoader userDataLoader,
            AccountDataLoader accountDataLoader,
            NotificationDataLoader notificationDataLoader,
            CardDataLoader cardDataLoader,
            SuggestedReserveDataLoader reservationTypeDataLoader,
            ReservationDataLoader reservationDataLoader,
            TransactionDataLoader transactionDataLoader,
            MovementDataLoader movementDataLoader) {

        return args -> {
            boolean isProduction = isProductionEnvironment();

            if (isProduction) {
                System.out.println("🔄 Iniciando carga de datos en modo producción...");

                userDataLoader.load();
                reservationTypeDataLoader.load();
                accountDataLoader.load();

                cardDataLoader.load();
                notificationDataLoader.load();
                reservationDataLoader.load();
                transactionDataLoader.load();
                movementDataLoader.load();

                System.out.println("✅ Carga de datos completada en producción");
            } else {
                System.out.println("🚧 Modo desarrollo - Carga completa de datos");
                userDataLoader.load();
                accountDataLoader.load();
                notificationDataLoader.load();
                cardDataLoader.load();
                transactionDataLoader.load();
                reservationTypeDataLoader.load();
                reservationDataLoader.load();
                movementDataLoader.load();
            }
        };
    }

    private boolean isProductionEnvironment() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if ("prod".equalsIgnoreCase(profile) || "production".equalsIgnoreCase(profile)) {
                return true;
            }
        }
        return false;
    }
}