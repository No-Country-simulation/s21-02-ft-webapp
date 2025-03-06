package com.wallex.financial_platform.controllers;

import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@AllArgsConstructor
public class TransactionController {
/*    private TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.getById(id));
    }

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> save(@RequestBody @Valid TransactionRequestDTO transaction) {
        return ResponseEntity.ok(transactionService.save(transaction));
    }*/

}
