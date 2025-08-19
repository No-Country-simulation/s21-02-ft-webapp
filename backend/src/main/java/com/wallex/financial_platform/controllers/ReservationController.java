package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;
import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.services.impl.AccountService;
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
    private final AccountService accountService;

    @GetMapping()
    public ResponseEntity<List<ReservationResponseDTO>> getActiveReservationsByAccount(@PathVariable Long accountId) {
        List<ReservationResponseDTO> reservations = reservationService.getActiveReservationsByAccount(accountId);
        return ResponseEntity.ok(reservations);
    }

    @PostMapping()
    public ResponseEntity<TransactionResponseDTO> reservation(
            @PathVariable Long accountId,
            @Valid @RequestBody ReservationRequestDTO reservationRequestDTO
    ) {
        TransactionResponseDTO responseDTO = accountService.createReservation(accountId, reservationRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("/{reservationId}/release")
    public ResponseEntity<TransactionResponseDTO> releaseReservation(@PathVariable Long reservationId, @PathVariable Long accountId) {
        TransactionResponseDTO response = accountService.releaseReservation(reservationId,accountId);
        return ResponseEntity.ok(response);
    }
}
