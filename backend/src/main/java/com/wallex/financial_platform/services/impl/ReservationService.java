package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;
import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
import com.wallex.financial_platform.exceptions.account.AccountErrorException;
import com.wallex.financial_platform.exceptions.account.AccountNotFoundException;
import com.wallex.financial_platform.exceptions.reservation.ReservationNotFoundException;
import com.wallex.financial_platform.exceptions.transaction.InsufficientFundsException;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.ReservationRepository;
import com.wallex.financial_platform.services.IReservationService;
import com.wallex.financial_platform.services.utils.UserContextService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationService implements IReservationService {
    private final ReservationRepository reservationRepository;
    private final AccountRepository accountRepository;
    private final UserContextService userContextService;

    @Transactional
    @Override
    public ReservationResponseDTO createReservation(ReservationRequestDTO reservationRequestDTO) {
        
        Account account = accountRepository.findById(reservationRequestDTO.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada"));

        User authenticatedUser = userContextService.getAuthenticatedUser();
        this.validateAccountOwnership(authenticatedUser, account);

        validateSufficientFunds(account, reservationRequestDTO.reservedAmount());

        Reservation reservation = Reservation.builder()
                .account(account)
                .reservedAmount(reservationRequestDTO.reservedAmount())
                .status(ReservationStatus.ACTIVE)
                .type(reservationRequestDTO.type())
                .build();

        account.setAvailableBalance(account.getAvailableBalance().subtract(reservationRequestDTO.reservedAmount()));
        accountRepository.save(account);

        reservation = reservationRepository.save(reservation);
        return mapToDTO(reservation);
    }

    @Transactional
    @Override
    public ReservationResponseDTO releaseReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva no encontrada"));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("La reserva no está activa");
        }

        Account account = reservation.getAccount();

        User authenticatedUser = userContextService.getAuthenticatedUser();
        this.validateAccountOwnership(authenticatedUser, account);

        account.setAvailableBalance(account.getAvailableBalance().add(reservation.getReservedAmount()));
        accountRepository.save(account);

        reservation.setStatus(ReservationStatus.RELEASED);
        reservation = reservationRepository.save(reservation);

        return mapToDTO(reservation);
    }

    @Override
    public List<ReservationResponseDTO> getActiveReservationsByAccount(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada"));

        User authenticatedUser = userContextService.getAuthenticatedUser();
        this.validateAccountOwnership(authenticatedUser, account);

        List<Reservation> reservations = reservationRepository.findByAccount_AccountIdAndStatus(accountId, ReservationStatus.ACTIVE);

        return reservations.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private void validateAccountOwnership(User authenticatedUser, Account account) {
        if (!account.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccountErrorException("No estás autorizado para operar esta cuenta.");
        }
    }
    private void validateSufficientFunds(Account account, BigDecimal amount) {
        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes para crear la reserva");
        }
    }

    private ReservationResponseDTO mapToDTO(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getReservationId(),
                reservation.getAccount().getAccountId(),
                reservation.getReservedAmount(),
                reservation.getCreationDate(),
                reservation.getStatus(),
                reservation.getType()
        );
    }
}
