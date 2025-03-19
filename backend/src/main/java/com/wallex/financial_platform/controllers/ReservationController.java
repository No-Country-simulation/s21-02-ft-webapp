package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;
import com.wallex.financial_platform.services.impl.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping()
    public ResponseEntity<List<ReservationResponseDTO>> getActiveReservationsByAccount(@PathVariable Long accountId) {
        List<ReservationResponseDTO> reservations = reservationService.getActiveReservationsByAccount(accountId);
        return ResponseEntity.ok(reservations);
    }
}
