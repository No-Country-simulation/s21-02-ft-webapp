package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.ReservationTypeRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationTypeResponseDTO;
import com.wallex.financial_platform.services.impl.ReservationService;
import com.wallex.financial_platform.services.impl.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/typeReservations")
@RequiredArgsConstructor
public class ReservationTypeController {
    private final ReservationService reservationService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ReservationTypeResponseDTO> createReservationType(
            @RequestParam("name") String name,
            @RequestParam("icon") MultipartFile icon) {

        ReservationTypeRequestDTO request = new ReservationTypeRequestDTO(name, icon);
        ReservationTypeResponseDTO response = reservationService.createTypeReservation(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ReservationTypeResponseDTO>> getAllReservationTypes() {
        List<ReservationTypeResponseDTO> types = reservationService.getAllReservationTypes();
        return ResponseEntity.ok(types);
    }
}
