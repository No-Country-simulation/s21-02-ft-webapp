package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.SuggestedReserveRequestDTO;
import com.wallex.financial_platform.dtos.responses.SuggestedReserveResponseDTO;
import com.wallex.financial_platform.services.impl.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/suggestedReserve")
@RequiredArgsConstructor
@Tag(name = "Reservas Sugeridas", description = "Gestión de tipos de reserva sugeridos")
public class SuggestedReserveController {
    private final ReservationService reservationService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Crear tipo de reserva sugerida",
            description = "Crea un nuevo tipo de reserva sugerida con nombre e icono"
    )
    public ResponseEntity<SuggestedReserveResponseDTO> createSuggestedReserve(
            @Parameter(description = "Nombre del tipo de reserva", example = "Vacaciones", required = true)
            @RequestParam("name") String name,

            @Parameter(
                    description = "Icono/imagen para el tipo de reserva",
                    required = true,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE)
            )
            @RequestParam("icon") MultipartFile icon) {

        SuggestedReserveRequestDTO request = new SuggestedReserveRequestDTO(name, icon);
        SuggestedReserveResponseDTO response = reservationService.createSuggestedReservations(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(
            summary = "Obtener tipos de reserva sugeridos",
            description = "Retorna todos los tipos de reserva sugeridos disponibles"
    )
    public ResponseEntity<List<SuggestedReserveResponseDTO>> getAllSuggestedReservations() {
        List<SuggestedReserveResponseDTO> types = reservationService.getAllSuggestedReservations();
        return ResponseEntity.ok(types);
    }
}