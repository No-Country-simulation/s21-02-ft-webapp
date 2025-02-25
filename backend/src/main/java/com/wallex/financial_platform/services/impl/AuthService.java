package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.requests.LoginRequestDTO;
import com.wallex.financial_platform.dtos.requests.RegisterUserRequestDTO;
import com.wallex.financial_platform.dtos.responses.UserResponseDTO;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.exceptions.auth.InvalidCredentialsException;
import com.wallex.financial_platform.exceptions.auth.UserAlreadyExistsException;
import com.wallex.financial_platform.exceptions.auth.UserNotFoundException;
import com.wallex.financial_platform.repositories.UserRepository;
import com.wallex.financial_platform.services.IAuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import com.wallex.financial_platform.configs.auth.JwtUtil;


@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public UserResponseDTO register(RegisterUserRequestDTO registerUserRequestDTO) {
        if (this.userRepository.findByEmail(registerUserRequestDTO.email()).isPresent()) {
            throw new UserAlreadyExistsException("El email ya está registrado");
        }

        User user = new User();
        user.setFullName(registerUserRequestDTO.fullName());
        user.setDni(registerUserRequestDTO.dni());
        user.setEmail(registerUserRequestDTO.email());
        user.setPhoneNumber(registerUserRequestDTO.phoneNumber());
        user.setPassword(passwordEncoder.encode(registerUserRequestDTO.password()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setActive(true);

        this.userRepository.save(user);

        return new UserResponseDTO(
                user.getId(), user.getFullName(), user.getDni(), user.getEmail(),
                user.getPhoneNumber(), user.getCreatedAt(), user.getUpdatedAt(), user.getActive()
        );
    }

    @Override
    public String login(LoginRequestDTO loginRequestDTO) {
        User user = this.userRepository.findByEmail(loginRequestDTO.email())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(loginRequestDTO.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Credenciales incorrectas");
        }

        return jwtUtil.generateToken(user.getEmail());
    }


}

