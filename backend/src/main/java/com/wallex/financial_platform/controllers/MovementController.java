package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.responses.MovementResponseDTO;
import com.wallex.financial_platform.services.impl.MovementService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/movements")
@AllArgsConstructor
public class MovementController {
 /*   private MovementService movementService;

    @GetMapping("/{movement_id}")
    private ResponseEntity<MovementResponseDTO> getMovementById(
            @PathVariable("movement_id") Long movementId
    ){
        return ResponseEntity.ok(movementService.getMovementById(movementId));
    }*/
}
