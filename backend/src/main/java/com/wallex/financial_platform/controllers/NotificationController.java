package com.wallex.financial_platform.controllers;


import com.wallex.financial_platform.dtos.responses.NotificationResponseDTO;
import com.wallex.financial_platform.services.impl.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationResponseDTO>>getNotificationsByUser() {
        List<NotificationResponseDTO> response = this.notificationService.getAllNotificationByUserOnline();
        return ResponseEntity.ok(response);
    }
}
