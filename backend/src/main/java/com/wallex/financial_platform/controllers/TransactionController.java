package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.services.impl.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
@AllArgsConstructor
@Tag(name = "Transacciones", description = "Consulta de historial de transacciones")
public class TransactionController {
    private TransactionService transactionService;

    @GetMapping
    @Operation(
            summary = "Obtener transacciones de cuenta",
            description = "Retorna el historial de transacciones de una cuenta específica"
    )
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionByAccount(
            @Parameter(description = "ID de la cuenta", example = "1", required = true)
            @PathVariable Long accountId) {
        List<TransactionResponseDTO> response = this.transactionService.getTransactionByAccount(accountId);
        return ResponseEntity.ok(response);
    }
}