package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.RegisterCardRequestDTO;
import com.wallex.financial_platform.dtos.requests.DniRequestDTO;
import com.wallex.financial_platform.dtos.responses.CardResponseDTO;
import com.wallex.financial_platform.services.impl.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/card")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping
    public ResponseEntity<List<CardResponseDTO>> getAllCards() {
        List<CardResponseDTO> response = this.cardService.getAllCards();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-dni")
    public ResponseEntity<List<CardResponseDTO>> getCardsByUserDni(@RequestBody DniRequestDTO dni) {
        List<CardResponseDTO> response = cardService.getCardsByUserDni(dni);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<CardResponseDTO> createCard(@RequestBody @Valid RegisterCardRequestDTO cardRequestDTO) {
        CardResponseDTO response = cardService.createCard(cardRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }
}
