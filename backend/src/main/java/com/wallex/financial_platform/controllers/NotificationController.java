package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.responses.NotificationResponseDTO;
import com.wallex.financial_platform.services.impl.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Gestión de notificaciones del usuario")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    @Operation(summary = "Obtener notificaciones",
            description = "Retorna todas las notificaciones del usuario autenticado")
    public ResponseEntity<List<NotificationResponseDTO>> getNotificationsByUser() {
        List<NotificationResponseDTO> response = this.notificationService.getAllNotificationByUserOnline();
        return ResponseEntity.ok(response);
    }
}