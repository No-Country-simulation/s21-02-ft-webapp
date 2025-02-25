package com.wallex.financial_platform.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.wallex.financial_platform.dtos.responses.UserResponseDTO;
import com.wallex.financial_platform.exceptions.auth.UserNotFoundException;
import com.wallex.financial_platform.services.IUserService;
import org.springframework.stereotype.Service;

import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.repositories.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        return this.userRepository.findByEmail(email).map(this::convertToDTO).orElseThrow(()->new UserNotFoundException("Usuario  con email " + email + " no encontrado"));
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        return this.userRepository.findById(id).map(this::convertToDTO).orElseThrow(()->new UserNotFoundException("Usuario  con id " + id + " no encontrado"));
    }

    @Override
    public UserResponseDTO getUserByDni(String dni) {
        return this.userRepository.findByDni(dni).map(this::convertToDTO).orElseThrow(()->new UserNotFoundException("Usuario  con dni " + dni + " no encontrado"));
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = this.userRepository.findAll();
        if(users.isEmpty()) {
            throw new UserNotFoundException("No hay usuarios registrados");
        }
        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByDni(String dni) {
        return this.userRepository.existsByDni(dni);
    }

    private UserResponseDTO convertToDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getDni(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getActive()
        );
    }
}