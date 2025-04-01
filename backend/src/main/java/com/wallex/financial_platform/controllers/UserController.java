package com.wallex.financial_platform.controllers;

import com.wallex.financial_platform.dtos.requests.DniRequestDTO;
import com.wallex.financial_platform.dtos.requests.EmailRequestDTO;
import com.wallex.financial_platform.dtos.responses.UserResponseDTO;
import com.wallex.financial_platform.services.impl.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wallex.financial_platform.services.impl.UserService;
import lombok.AllArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor

public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUserOnline() {
        List<UserResponseDTO> response = userService.getUserOnline();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-dni")
    public ResponseEntity<UserResponseDTO> getUserByDni(@RequestBody DniRequestDTO dniRequestDTO) {
        UserResponseDTO response = userService.getUserByDni(dniRequestDTO.dni());
        return ResponseEntity.ok(response);
    }

}