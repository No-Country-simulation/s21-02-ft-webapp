package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.responses.TransactionResponseDTO;
import com.wallex.financial_platform.services.impl.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class TransactionController {
private TransactionService transactionService;

    @GetMapping("/account/{id}")
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionByAccount(@PathVariable Long id) {
       List<TransactionResponseDTO> response = this.transactionService.getTransactionByAccount(id);
        return ResponseEntity.ok(response);
    }
/*
    @PostMapping
    public ResponseEntity<TransactionResponseDTO> save(@RequestBody @Valid TransactionRequestDTO transaction) {
        return ResponseEntity.ok(transactionService.save(transaction));
    }*/

}
