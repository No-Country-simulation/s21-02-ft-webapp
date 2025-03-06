package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.MovementRequestDTO;
import com.wallex.financial_platform.dtos.responses.MovementResponseDTO;
import com.wallex.financial_platform.services.impl.MovementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movements")
@AllArgsConstructor
public class MovementController {

 private final MovementService movementService;

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<MovementResponseDTO>> getMovementsByAccount(@PathVariable Long accountId) {
        List<MovementResponseDTO> movements = movementService.getMovementsByAccount(accountId);
        return ResponseEntity.ok(movements);
    }

}
