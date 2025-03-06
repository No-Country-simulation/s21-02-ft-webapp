package com.wallex.financial_platform.controllers;

import java.util.List;


import com.wallex.financial_platform.dtos.requests.AccountRequestDTO;
import com.wallex.financial_platform.dtos.requests.DepositRequestDTO;
import com.wallex.financial_platform.dtos.requests.TransferRequestDTO;
import com.wallex.financial_platform.dtos.responses.*;
import com.wallex.financial_platform.services.impl.MovementService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wallex.financial_platform.services.impl.AccountService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {
    private AccountService accountService;
    private MovementService movementService;

    @GetMapping("/all")
    public ResponseEntity<List<AccountResponseDTO>> getAccountsAll() {
        List<AccountResponseDTO>response = this.accountService.getAccountsByUserAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAccounts() {
        List<AccountResponseDTO>response = this.accountService.getAccountsByUser();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody @Valid AccountRequestDTO accountReq) {
        AccountResponseDTO response = this.accountService.createAccount(accountReq);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(
            @RequestParam Long sourceAccountId, // Recibe el ID como parámetro de consulta
            @Valid @RequestBody TransferRequestDTO transferRequestDTO) { // El resto va en el body
        TransactionResponseDTO responseDTO = accountService.transfer(sourceAccountId, transferRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@RequestBody DepositRequestDTO depositRequestDTO) {
        TransactionResponseDTO responseDTO = accountService.addFundsFromCard(depositRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

   /* @PostMapping("/check")
    public ResponseEntity<CheckAccountResponseDTO> createDestinationAccount(@RequestBody @Valid CheckAccountRequestDto accountData) {
        if (accountData.alias().isBlank() && accountData.cbu().isBlank()) {
            throw new IllegalArgumentException("Alias or cbu is required");
        }
        return ResponseEntity.ok(accountService.checkAccount(accountData));
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionResumeResponseDTO>> getTransactions(
            @PathVariable("id") Long accountId,
            @RequestParam(required = false) @PastOrPresent Date from,
            @RequestParam(required = false) @PastOrPresent Date to,
            @RequestParam(required = false) TransactionStatus status
    ) {
        return ResponseEntity.ok(accountService.getTransactions(accountId));
    }

    @GetMapping("/{id}/reservations")
    public ResponseEntity<List<ReservationResponseDto>> getReservations(
            @PathVariable("id") Long accountId
    ) {
        return ResponseEntity.ok(accountService.getReservations(accountId));
    }

    @GetMapping("/{id}/movements")
    public ResponseEntity<List<MovementResponseDTO>> getMovementsByAccount(@PathVariable("id") Long accountId) {
        return ResponseEntity.ok(movementService.getUserAccountMovements(accountId));
    }*/

    @GetMapping("/currencies")
    public ResponseEntity<List<String>> getCurrencies() {
        List<String> currencies = this.accountService.getCurrencies();
        return ResponseEntity.ok(currencies);
    }
}
