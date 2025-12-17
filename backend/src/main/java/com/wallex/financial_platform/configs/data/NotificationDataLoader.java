package com.wallex.financial_platform.configs.data;

import com.wallex.financial_platform.entities.Notification;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.NotificationStatus;
import com.wallex.financial_platform.entities.enums.NotificationType;
import com.wallex.financial_platform.repositories.NotificationRepository;
import com.wallex.financial_platform.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationDataLoader {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void load() {
        List<User> users = findUsersForNotifications();

        if (users.isEmpty()) {
            System.out.println("⚠️ No se encontraron usuarios para crear notificaciones");
            return;
        }

        List<Notification> notifications = new ArrayList<>();

        // Crear notificaciones para los usuarios encontrados
        for (int i = 0; i < Math.min(users.size(), 3); i++) {
            User user = users.get(i);

            notifications.add(new Notification(
                    null,
                    user,
                    NotificationType.EMAIL,
                    "Mensaje informativo para " + user.getFullName(),
                    LocalDateTime.now(),
                    NotificationStatus.SENT
            ));

            if (i == 0) {
                notifications.add(new Notification(
                        null,
                        user,
                        NotificationType.SMS,
                        "Mensaje de alerta para " + user.getFullName(),
                        LocalDateTime.now(),
                        NotificationStatus.READ
                ));
            }
        }

        if (!notifications.isEmpty()) {
            notificationRepository.saveAll(notifications);
            System.out.println("✅ " + notifications.size() + " notificaciones creadas");
        }
    }

    private List<User> findUsersForNotifications() {
        List<User> users = new ArrayList<>();

        String[] testEmails = {
                "jindrg@gmail.com",
                "gusti.paz11@gmail.com",
                "gastongomez2014@hotmail.com",
                "sebastian.tournier11@gmail.com",
                "luis.mendez@dominio.com",
                "tesoreria@wallex.com"
        };

        for (String email : testEmails) {
            userRepository.findByEmail(email)
                    .ifPresent(users::add);
        }

        if (users.isEmpty()) {
            List<User> firstUsers = userRepository.findFirst3ByOrderByCreatedAtAsc();
            users.addAll(firstUsers);
        }

        return users;
    }
}