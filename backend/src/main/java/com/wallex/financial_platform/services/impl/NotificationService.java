package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.responses.NotificationResponseDTO;
import com.wallex.financial_platform.entities.Notification;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.NotificationStatus;
import com.wallex.financial_platform.entities.enums.NotificationType;
import com.wallex.financial_platform.events.NotificationEvent;
import com.wallex.financial_platform.exceptions.notification.NotificationException;
import com.wallex.financial_platform.repositories.NotificationRepository;
import com.wallex.financial_platform.services.INotificationService;
import com.wallex.financial_platform.services.utils.UserContextService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final UserContextService userContextService;

    @Transactional
    public void notifyUser(User user, String ignoredSubject, String message) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setType(NotificationType.EMAIL);
        notification.setMessage(message);
        notification.setStatus(NotificationStatus.PENDING);
        notification.setSendingDate(LocalDateTime.now());

        notificationRepository.save(notification);

        eventPublisher.publishEvent(new NotificationEvent(this, notification));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDTO> getAllNotificationByUserOnline() {

            List<Notification> notifications = this.notificationRepository.findByUserId(userContextService.getAuthenticatedUser().getId());
            if (notifications.isEmpty()) {
                throw new NotificationException("No hay tarjetas asociadas a este usuario.", null);
            }

            return notifications.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

    }

    private NotificationResponseDTO convertToDTO(Notification notification) {
        return new NotificationResponseDTO(
                notification.getSendingDate(),
                notification.getMessage(),
                notification.getStatus(),
                notification.getType()
        );
    }

}
