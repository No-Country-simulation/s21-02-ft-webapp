package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.requests.SuggestedReserveRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;
import com.wallex.financial_platform.dtos.responses.SuggestedReserveResponseDTO;

import java.util.List;

public interface IReservationService {
    void createReservation(Long accountId, ReservationRequestDTO reservationRequestDTO);
    ReservationResponseDTO releaseReservation(Long reservationId,Long accountId);
    List<ReservationResponseDTO> getActiveReservationsByAccount(Long accountId);
    SuggestedReserveResponseDTO createSuggestedReservations(SuggestedReserveRequestDTO reservationTypeRequestDTO);
    List<SuggestedReserveResponseDTO> getAllSuggestedReservations();
}
