package com.wallex.financial_platform.dtos.responses;

import com.wallex.financial_platform.entities.enums.NotificationStatus;
import com.wallex.financial_platform.entities.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Información de notificación")
public record NotificationResponseDTO(
        @Schema(description = "Fecha y hora de envío", example = "2024-01-15T10:30:00")
        LocalDateTime sendingDate,

        @Schema(description = "Mensaje de la notificación", example = "Transferencia recibida por $1,500.75")
        String message,

        @Schema(description = "Estado de la notificación", example = "UNREAD")
        NotificationStatus notificationStatus,

        @Schema(description = "Tipo de notificación", example = "TRANSACTION")
        NotificationType notificationType
) {
}