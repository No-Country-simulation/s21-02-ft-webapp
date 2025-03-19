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
import java.util.List;

@Component
@RequiredArgsConstructor
public class NotificationDataLoader {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public void load() {
        User user1 = userRepository.findById(1L).orElseThrow();
        User user2 = userRepository.findById(2L).orElseThrow();
        User user3 = userRepository.findById(4L).orElseThrow();

        Notification notification1 = new Notification(
                null,
                user1,
                NotificationType.EMAIL,
                "Este es un mensaje informativo",
                LocalDateTime.now(),
                NotificationStatus.READ
        );

        Notification notification2 = new Notification(
                null,
                user1,
                NotificationType.SMS,
                "Este es un mensaje de alerta",
                LocalDateTime.now(),
                NotificationStatus.SENT
        );

        // Crear las notificaciones para el usuario 2
        Notification notification3 = new Notification(
                null,
                user2,
                NotificationType.EMAIL,
                "Este es un mensaje de advertencia",
                LocalDateTime.now(),
                NotificationStatus.SENT
        );

        Notification notification4 = new Notification(
                null,
                user3,
                NotificationType.EMAIL,
                "Este es otro mensaje informativo",
                LocalDateTime.now(),
                NotificationStatus.SENT
        );

        notificationRepository.saveAll(List.of(notification1, notification2, notification3, notification4));
    }
}
