package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDto;
import com.wallex.financial_platform.services.impl.ReservationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
@AllArgsConstructor
public class ReservationController {
    private ReservationService reservationService;

   /* @PostMapping
    private ResponseEntity<ReservationResponseDto> postReservation(@RequestBody @Valid ReservationRequestDTO reservation){
        return ResponseEntity.ok(reservationService.saveReservation(reservation));
    }*/
}
