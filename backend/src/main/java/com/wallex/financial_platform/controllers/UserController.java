package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.DniRequestDTO;
import com.wallex.financial_platform.dtos.responses.UserResponseDTO;
import com.wallex.financial_platform.services.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
@Tag(name = "Usuarios", description = "Consulta de información de usuarios")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(
            summary = "Obtener usuario actual",
            description = "Retorna la información del usuario autenticado"
    )
    public ResponseEntity<List<UserResponseDTO>> getUserOnline() {
        List<UserResponseDTO> response = userService.getUserOnline();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-dni")
    @Operation(
            summary = "Buscar usuario por DNI",
            description = "Busca un usuario por su número de DNI"
    )
    public ResponseEntity<UserResponseDTO> getUserByDni(@RequestBody DniRequestDTO dniRequestDTO) {
        UserResponseDTO response = userService.getUserByDni(dniRequestDTO.dni());
        return ResponseEntity.ok(response);
    }
}