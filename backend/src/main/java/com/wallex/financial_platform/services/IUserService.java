package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.responses.UserResponseDTO;

import java.util.List;

public interface IUserService {
    UserResponseDTO getUserByDni(String username);
    List<UserResponseDTO> getUserOnline();
}
