package com.wallex.financial_platform.controllers;

import java.math.BigDecimal;
import java.util.List;
import com.wallex.financial_platform.dtos.requests.AccountRequestDTO;
import com.wallex.financial_platform.dtos.requests.DepositRequestDTO;
import com.wallex.financial_platform.dtos.requests.TransferRequestDTO;
import com.wallex.financial_platform.dtos.responses.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wallex.financial_platform.services.impl.AccountService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
@Tag(name = "Cuentas", description = "Gestión de cuentas bancarias y transacciones")
public class AccountController {
    private AccountService accountService;

    @GetMapping("/all")
    @Operation(summary = "Obtener todas las cuentas",
            description = "Retorna todas las cuentas del sistema (solo administradores)")
    public ResponseEntity<List<AccountResponseDTO>> getAccountsAll() {
        List<AccountResponseDTO>response = this.accountService.getAccountsByUserAll();
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Obtener cuentas del usuario",
            description = "Retorna las cuentas del usuario autenticado")
    public ResponseEntity<List<AccountResponseDTO>> getAccounts() {
        List<AccountResponseDTO>response = this.accountService.getAccountsByUser();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "Crear nueva cuenta",
            description = "Crea una nueva cuenta bancaria para el usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cuenta creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AccountResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    public ResponseEntity<AccountResponseDTO> createAccount(@RequestBody @Valid AccountRequestDTO accountReq) {
        AccountResponseDTO response = this.accountService.createAccount(accountReq);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/transfer")
    @Operation(summary = "Realizar transferencia",
            description = "Transfiere dinero entre cuentas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transferencia realizada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Saldo insuficiente o datos inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponseDTO.class))),
    })
    public ResponseEntity<TransactionResponseDTO> transfer(
            @Parameter(description = "ID de la cuenta de origen", required = true, example = "1")
            @RequestParam Long sourceAccountId,
            @Valid @RequestBody TransferRequestDTO transferRequestDTO) {
        TransactionResponseDTO responseDTO = accountService.transfer(sourceAccountId, transferRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/deposit")
    @Operation(summary = "Depositar fondos",
            description = "Deposita dinero desde una tarjeta a una cuenta")
    public ResponseEntity<TransactionResponseDTO> deposit(
            @Parameter(description = "ID de la cuenta destino", required = true, example = "1")
            @RequestParam Long sourceAccountId,
            @Valid @RequestBody DepositRequestDTO depositRequestDTO) {
        TransactionResponseDTO responseDTO = accountService.addFundsFromCard(sourceAccountId, depositRequestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/currencies")
    @Operation(summary = "Obtener tipos de moneda",
            description = "Retorna la lista de monedas disponibles")
    public ResponseEntity<List<String>> getCurrencies() {
        List<String> currencies = this.accountService.getCurrencies();
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validar cuenta destino",
            description = "Valida si un identificador (CBU/Alias) corresponde a una cuenta válida")
    public ResponseEntity<ValidateTransferResultResponseDTO> validateDestination(
            @Parameter(description = "CBU o Alias de la cuenta a validar", required = true, example = "0000003100035587501234")
            @RequestParam String destination) {
        ValidateTransferResultResponseDTO result = accountService.validateAccountIdentifier(destination);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{accountId}/check-balance")
    @Operation(summary = "Verificar saldo",
            description = "Verifica si una cuenta tiene saldo suficiente para una operación")
    public ResponseEntity<BalanceCheckResponseDTO> checkBalance(
            @Parameter(description = "ID de la cuenta", required = true, example = "1")
            @PathVariable Long accountId,
            @Parameter(description = "Monto a verificar", required = true, example = "1500.50")
            @RequestParam BigDecimal amount) {

        BalanceCheckResponseDTO response = accountService.checkAccountBalance(accountId, amount);
        return ResponseEntity.ok(response);
    }
}