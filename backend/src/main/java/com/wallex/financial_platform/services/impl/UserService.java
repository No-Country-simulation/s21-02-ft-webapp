package com.wallex.financial_platform.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.wallex.financial_platform.dtos.responses.UserResponseDTO;
import com.wallex.financial_platform.exceptions.auth.UserNotFoundException;
import com.wallex.financial_platform.services.IUserService;
import com.wallex.financial_platform.services.utils.UserContextService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.repositories.UserRepository;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final UserContextService userContextService;

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByDni(String dni) {
        User user = userContextService.validateUserAccess(dni); 
        return convertToDTO(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getUserOnline() {
        User user = this.userRepository.findById(this.userContextService.getAuthenticatedUser().getId())
                .orElseThrow(() -> new UserNotFoundException("No hay usuarios registrados"));
        return List.of(convertToDTO(user));
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