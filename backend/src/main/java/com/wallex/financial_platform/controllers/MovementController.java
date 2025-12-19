package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.responses.MovementResponseDTO;
import com.wallex.financial_platform.services.impl.MovementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/accounts/{accountId}/movements")
@AllArgsConstructor
@Tag(name = "Movimientos", description = "Consulta de movimientos de cuentas")
public class MovementController {

    private final MovementService movementService;

    @GetMapping
    @Operation(summary = "Obtener movimientos de cuenta",
            description = "Retorna el historial de movimientos de una cuenta específica")
    public ResponseEntity<List<MovementResponseDTO>> getMovementsByAccount(
            @Parameter(description = "ID de la cuenta", example = "1", required = true)
            @PathVariable Long accountId) {
        List<MovementResponseDTO> movements = movementService.getMovementsByAccount(accountId);
        return ResponseEntity.ok(movements);
    }
}