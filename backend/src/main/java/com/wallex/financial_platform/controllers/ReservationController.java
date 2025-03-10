package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;
import com.wallex.financial_platform.services.impl.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationResponseDTO> createReservation( @PathVariable Long accountId, @RequestBody @Valid ReservationRequestDTO reservationRequestDTO) {
        ReservationResponseDTO response = reservationService.createReservation(accountId, reservationRequestDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{reservationId}/release")
    public ResponseEntity<ReservationResponseDTO> releaseReservation(@PathVariable Long reservationId, @PathVariable Long accountId) {
        ReservationResponseDTO response = reservationService.releaseReservation(reservationId,accountId);
        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<List<ReservationResponseDTO>> getActiveReservationsByAccount(@PathVariable Long accountId) {
        List<ReservationResponseDTO> reservations = reservationService.getActiveReservationsByAccount(accountId);
        return ResponseEntity.ok(reservations);
    }
}
