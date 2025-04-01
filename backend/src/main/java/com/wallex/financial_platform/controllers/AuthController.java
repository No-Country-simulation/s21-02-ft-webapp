package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.LoginRequestDTO;
import com.wallex.financial_platform.dtos.requests.RegisterUserRequestDTO;
import com.wallex.financial_platform.dtos.responses.AuthResponseDTO;
import com.wallex.financial_platform.dtos.responses.UserResponseDTO;
import com.wallex.financial_platform.services.impl.AuthService;
import com.wallex.financial_platform.services.impl.JwtBlacklistService;
import jakarta.validation.Valid;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtBlacklistService jwtBlacklistService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid RegisterUserRequestDTO registerUserRequestDTO) {
        AuthResponseDTO user = authService.register(registerUserRequestDTO);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO) {
        AuthResponseDTO login = authService.login(loginRequestDTO);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        jwtBlacklistService.invalidateToken(token); // Agregar el token a la lista negra
        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }


    @GetMapping("/test")
    public ResponseEntity<String> testConnection() {
        return ResponseEntity.ok("Conexión exitosa con Spring Boot!");
    }

}
