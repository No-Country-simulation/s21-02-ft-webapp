package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.services.impl.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/accounts/{accountId}/transactions")
@AllArgsConstructor
public class TransactionController {
private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionByAccount( @PathVariable Long accountId) {
       List<TransactionResponseDTO> response = this.transactionService.getTransactionByAccount(accountId);
        return ResponseEntity.ok(response);
    }

}
