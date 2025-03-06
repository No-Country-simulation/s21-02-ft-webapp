package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.requests.MovementRequestDTO;
import com.wallex.financial_platform.dtos.responses.MovementResponseDTO;
import com.wallex.financial_platform.entities.Transaction;

import java.util.List;

public interface IMovementService {

    MovementResponseDTO createMovement(MovementRequestDTO movementRequestDTO);
    //MovementResponseDTO getMovementById(Long movementId);
    //List<MovementResponseDTO> getUserAccountMovements(Long accountId);
    List<MovementResponseDTO> getMovementsByAccount(Long accountId);
}
