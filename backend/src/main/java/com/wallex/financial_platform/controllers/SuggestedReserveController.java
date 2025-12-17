package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.SuggestedReserveRequestDTO;
import com.wallex.financial_platform.dtos.responses.SuggestedReserveResponseDTO;
import com.wallex.financial_platform.services.impl.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/suggestedReserve")
@RequiredArgsConstructor
public class SuggestedReserveController {
    private final ReservationService reservationService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SuggestedReserveResponseDTO> createSuggestedReserve(
            @RequestParam("name") String name,
            @RequestParam("icon") MultipartFile icon) {

        SuggestedReserveRequestDTO request = new SuggestedReserveRequestDTO(name, icon);
        SuggestedReserveResponseDTO response = reservationService.createSuggestedReservations(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<SuggestedReserveResponseDTO>> getAllSuggestedReservations() {
        List<SuggestedReserveResponseDTO> types = reservationService.getAllSuggestedReservations();
        return ResponseEntity.ok(types);
    }
}
