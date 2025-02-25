package com.wallex.financial_platform.repositories;

import com.wallex.financial_platform.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
