package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.requests.ReservationTypeRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;
import com.wallex.financial_platform.dtos.responses.ReservationTypeResponseDTO;

import java.util.List;

public interface IReservationService {
    void createReservation(Long accountId, ReservationRequestDTO reservationRequestDTO);
    ReservationResponseDTO releaseReservation(Long reservationId,Long accountId);
    List<ReservationResponseDTO> getActiveReservationsByAccount(Long accountId);
    ReservationTypeResponseDTO  createTypeReservation(ReservationTypeRequestDTO reservationTypeRequestDTO);
    List<ReservationTypeResponseDTO> getAllReservationTypes();
}
