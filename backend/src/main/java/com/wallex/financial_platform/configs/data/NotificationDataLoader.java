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

        LocalDateTime date1 = LocalDateTime.of(2025, 3, 2, 10, 0); // 2 de enero de 2025, 10:00 AM
        LocalDateTime date2 = LocalDateTime.of(2025, 3, 8, 14, 30); // 15 de enero de 2025, 2:30 PM
        LocalDateTime date3 = LocalDateTime.of(2025, 3, 6, 9, 15); // 1 de febrero de 2025, 9:15 AM
        LocalDateTime date4 = LocalDateTime.of(2025, 3, 1, 18, 45); // 11 de marzo de 2025, 6:45 PM

        Notification notification1 = new Notification(
                null,
                user1,
                NotificationType.EMAIL,
                "Este es un mensaje informativo",
                date1,
                NotificationStatus.SENT
        );

        Notification notification2 = new Notification(
                null,
                user1,
                NotificationType.EMAIL,
                "Este es un mensaje de alerta",
                date2,
                NotificationStatus.SENT
        );

        Notification notification3 = new Notification(
                null,
                user2,
                NotificationType.EMAIL,
                "Este es un mensaje de advertencia",
                date3,
                NotificationStatus.SENT
        );

        Notification notification4 = new Notification(
                null,
                user3,
                NotificationType.EMAIL,
                "Este es otro mensaje informativo",
                date4,
                NotificationStatus.SENT
        );

        notificationRepository.saveAll(List.of(notification1, notification2, notification3, notification4));
    }
}