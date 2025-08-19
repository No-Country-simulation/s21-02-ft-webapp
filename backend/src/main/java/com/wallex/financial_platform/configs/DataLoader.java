package com.wallex.financial_platform.configs;

import com.wallex.financial_platform.configs.data.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {
    @Bean
    public CommandLineRunner loadData(UserDataLoader userDataLoader,
                                      AccountDataLoader accountDataLoader,
                                      NotificationDataLoader notificationDataLoader,
                                      CardDataLoader cardDataLoader,
                                      SuggestedReserveDataLoader reservationTypeDataLoader, // <-- acá
                                      ReservationDataLoader reservationDataLoader,
                                      TransactionDataLoader transactionDataLoader,
                                      MovementDataLoader movementDataLoader) {
        return args -> {
            userDataLoader.load();
            accountDataLoader.load();
            notificationDataLoader.load();
            cardDataLoader.load();
            transactionDataLoader.load();

            reservationTypeDataLoader.load();   // <-- Cargar tipos de reserva primero
            reservationDataLoader.load();       // <-- Luego cargar reservas

            movementDataLoader.load();
        };
    }

}
