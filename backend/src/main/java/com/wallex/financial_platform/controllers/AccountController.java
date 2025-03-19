package com.wallex.financial_platform.controllers;

import java.util.List;


import com.wallex.financial_platform.dtos.requests.AccountRequestDTO;
import com.wallex.financial_platform.dtos.requests.DepositRequestDTO;
import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
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
    public ResponseEntity<TransactionResponseDTO> transfer(@RequestParam Long sourceAccountId, @Valid @RequestBody TransferRequestDTO transferRequestDTO) {
        TransactionResponseDTO responseDTO = accountService.transfer(sourceAccountId, transferRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@RequestParam Long sourceAccountId, @Valid @RequestBody DepositRequestDTO depositRequestDTO) {
        TransactionResponseDTO responseDTO = accountService.addFundsFromCard(sourceAccountId, depositRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/reservation")
    public ResponseEntity<TransactionResponseDTO> reservation(@RequestParam Long sourceAccountId, @Valid @RequestBody ReservationRequestDTO reservationRequestDTO) {
        TransactionResponseDTO responseDTO = accountService.createReservation(sourceAccountId, reservationRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/{accountId}/reservations/{reservationId}/release")
    public ResponseEntity<TransactionResponseDTO> releaseReservation(@PathVariable Long reservationId, @PathVariable Long accountId) {
        TransactionResponseDTO response = accountService.releaseReservation(reservationId,accountId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/currencies")
    public ResponseEntity<List<String>> getCurrencies() {
        List<String> currencies = this.accountService.getCurrencies();
        return ResponseEntity.ok(currencies);
    }
}
