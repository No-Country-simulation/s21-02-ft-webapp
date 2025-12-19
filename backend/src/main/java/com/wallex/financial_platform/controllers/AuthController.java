package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.LoginRequestDTO;
import com.wallex.financial_platform.dtos.requests.RegisterUserRequestDTO;
import com.wallex.financial_platform.dtos.responses.AuthResponseDTO;
import com.wallex.financial_platform.services.impl.AuthService;
import com.wallex.financial_platform.services.impl.JwtBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Registro, login y logout de usuarios")
public class AuthController {
    private final AuthService authService;
    private final JwtBlacklistService jwtBlacklistService;

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Crea una nueva cuenta de usuario")
    public ResponseEntity<AuthResponseDTO> register(
            @RequestBody @Valid RegisterUserRequestDTO registerUserRequestDTO) {
        AuthResponseDTO user = authService.register(registerUserRequestDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica usuario y devuelve token JWT")
    public ResponseEntity<AuthResponseDTO> login(
            @RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        AuthResponseDTO login = authService.login(loginRequestDTO);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión", description = "Invalida el token JWT actual")
    public ResponseEntity<String> logout(
            @Parameter(description = "Token JWT con formato: Bearer {token}",
                    example = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
            @RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        jwtBlacklistService.invalidateToken(token);
        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }

    @GetMapping("/test")
    @Operation(summary = "Probar conexión", description = "Verifica que la API está funcionando")
    public ResponseEntity<String> testConnection() {
        return ResponseEntity.ok("Conexión exitosa con Spring Boot!");
    }
}