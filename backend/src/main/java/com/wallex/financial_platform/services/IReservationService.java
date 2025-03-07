package com.wallex.financial_platform.services;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;

import java.util.List;

public interface IReservationService {
    //List<ReservationResponseDto> getReservationsByAccountId(Long accountId);
    //ReservationResponseDto saveReservation(ReservationRequestDTO reservationReq);
    ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO);
    ReservationResponseDTO releaseReservation(Long reservationId);
    List<ReservationResponseDTO> getActiveReservationsByAccount(Long accountId);
}
