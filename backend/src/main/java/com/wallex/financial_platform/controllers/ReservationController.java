package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;
import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.services.impl.AccountService;
import com.wallex.financial_platform.services.impl.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/reservations")
@RequiredArgsConstructor
@Tag(name = "Reservas", description = "Gestión de reservas de fondos en cuentas")
public class ReservationController {
    private final ReservationService reservationService;
    private final AccountService accountService;

    @GetMapping()
    @Operation(summary = "Obtener reservas activas",
            description = "Retorna las reservas de fondos activas de una cuenta")
    public ResponseEntity<List<ReservationResponseDTO>> getActiveReservationsByAccount(
            @Parameter(description = "ID de la cuenta", example = "1", required = true)
            @PathVariable Long accountId) {
        List<ReservationResponseDTO> reservations = reservationService.getActiveReservationsByAccount(accountId);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping()
    @Operation(summary = "Crear reserva",
            description = "Reserva fondos en una cuenta para futura transacción")
    public ResponseEntity<TransactionResponseDTO> reservation(
            @Parameter(description = "ID de la cuenta", example = "1", required = true)
            @PathVariable Long accountId,
            @Valid @RequestBody ReservationRequestDTO reservationRequestDTO) {
        TransactionResponseDTO responseDTO = accountService.createReservation(accountId, reservationRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{reservationId}/release")
    @Operation(summary = "Liberar reserva",
            description = "Libera los fondos previamente reservados")
    public ResponseEntity<TransactionResponseDTO> releaseReservation(
            @Parameter(description = "ID de la reserva", example = "10", required = true)
            @PathVariable Long reservationId,
            @Parameter(description = "ID de la cuenta", example = "1", required = true)
            @PathVariable Long accountId) {
        TransactionResponseDTO response = accountService.releaseReservation(reservationId, accountId);
        return ResponseEntity.ok(response);
    }
}