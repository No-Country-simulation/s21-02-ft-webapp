package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.RegisterCardRequestDTO;
import com.wallex.financial_platform.dtos.requests.DniRequestDTO;
import com.wallex.financial_platform.dtos.responses.CardResponseDTO;
import com.wallex.financial_platform.services.impl.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Tarjetas", description = "Gestión de tarjetas de crédito/débito")
public class CardController {
    private final CardService cardService;

    @GetMapping
    @Operation(summary = "Obtener tarjetas del usuario",
            description = "Retorna todas las tarjetas del usuario autenticado")
    public ResponseEntity<List<CardResponseDTO>> getAllCardsByUserOnline() {
        List<CardResponseDTO> response = this.cardService.getAllCardsByUserOnline();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-dni")
    @Operation(summary = "Buscar tarjetas por DNI",
            description = "Busca tarjetas asociadas a un DNI específico")
    public ResponseEntity<List<CardResponseDTO>> getCardsByUserDni(
            @RequestBody DniRequestDTO dni) {
        List<CardResponseDTO> response = cardService.getCardsByUserDni(dni);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar nueva tarjeta",
            description = "Agrega una nueva tarjeta al perfil del usuario")
    public ResponseEntity<CardResponseDTO> createCard(
            @RequestBody @Valid RegisterCardRequestDTO cardRequestDTO) {
        CardResponseDTO response = cardService.createCard(cardRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cardId}")
    @Operation(summary = "Eliminar tarjeta",
            description = "Elimina una tarjeta específica por su ID")
    public ResponseEntity<Void> deleteCard(
            @Parameter(description = "ID de la tarjeta a eliminar", example = "1")
            @PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/types")
    @Operation(summary = "Obtener tipos de tarjeta",
            description = "Retorna los tipos de tarjetas disponibles")
    public ResponseEntity<List<String>> getCardTypes() {
        List<String> cardTypes = this.cardService.getAllCardTypes();
        return ResponseEntity.ok(cardTypes);
    }
}