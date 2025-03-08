package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;
import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
import com.wallex.financial_platform.entities.enums.TypeReservation;
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
import java.util.Optional;
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
        Account account = this.validateAndGetAccount(reservationRequestDTO.accountId());
        this.validateAccountOwnership(account);

        Optional<Reservation> existingReservation = findExistingReservation(
                reservationRequestDTO.accountId(),
                reservationRequestDTO.type().name()
        );

        this.validateSufficientFunds(account, reservationRequestDTO.reservedAmount());

        Reservation reservation = existingReservation
                .map(res -> this.updateReservationAmount(res, reservationRequestDTO.reservedAmount()))
                .orElseGet(() -> this.createNewReservation(account, reservationRequestDTO));

        this.updateAccountBalance(account, reservationRequestDTO.reservedAmount().negate());
        reservation = saveReservation(reservation);

        return this.mapToDTO(reservation);
    }

    @Transactional
    @Override
    public ReservationResponseDTO releaseReservation(Long reservationId) {
        Reservation reservation = this.validateAndGetReservation(reservationId);
        this.validateReservationStatus(reservation);

        Account account = reservation.getAccount();
        this.validateAccountOwnership(account);

        this.updateAccountBalance(account, reservation.getReservedAmount());
        reservation.setStatus(ReservationStatus.RELEASED);
        reservation = this.saveReservation(reservation);

        return this.mapToDTO(reservation);
    }

    @Override
    public List<ReservationResponseDTO> getActiveReservationsByAccount(Long accountId) {
        Account account = this.validateAndGetAccount(accountId);
        this.validateAccountOwnership(account);

        List<Reservation> reservations = findActiveReservationsByAccount(accountId);
        return this.mapReservationsToDTOs(reservations);
    }

    // ========== Métodos de Validación ==========

    private Account validateAndGetAccount(Long accountId) {
        return this.accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada"));
    }

    private Reservation validateAndGetReservation(Long reservationId) {
        return this.reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva no encontrada"));
    }

    private void validateAccountOwnership(Account account) {
        User authenticatedUser = this.userContextService.getAuthenticatedUser();
        if (!account.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccountErrorException("No estás autorizado para operar esta cuenta.");
        }
    }

    private void validateReservationStatus(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("La reserva no está activa");
        }
    }

    private void validateSufficientFunds(Account account, BigDecimal amount) {
        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes para crear la reserva");
        }
    }

    // ========== Métodos de Búsqueda ==========

    private Optional<Reservation> findExistingReservation(Long accountId, String type) {
        return this.reservationRepository.findByAccount_AccountIdAndTypeAndStatus(
                accountId,
                TypeReservation.valueOf(type),
                ReservationStatus.ACTIVE
        );
    }

    private List<Reservation> findActiveReservationsByAccount(Long accountId) {
        return this.reservationRepository.findByAccount_AccountIdAndStatus(accountId, ReservationStatus.ACTIVE);
    }

    // ========== Métodos de Creación y Actualización ==========

    private Reservation createNewReservation(Account account, ReservationRequestDTO reservationRequestDTO) {
        return Reservation.builder()
                .account(account)
                .reservedAmount(reservationRequestDTO.reservedAmount())
                .status(ReservationStatus.ACTIVE)
                .type(reservationRequestDTO.type())
                .build();
    }

    private Reservation updateReservationAmount(Reservation reservation, BigDecimal amount) {
        reservation.setReservedAmount(reservation.getReservedAmount().add(amount));
        return reservation;
    }

    private void updateAccountBalance(Account account, BigDecimal amount) {
        account.setAvailableBalance(account.getAvailableBalance().add(amount));
        this.accountRepository.save(account);
    }

    private Reservation saveReservation(Reservation reservation) {
        return this.reservationRepository.save(reservation);
    }

    // ========== Métodos de Mapeo ==========

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

    private List<ReservationResponseDTO> mapReservationsToDTOs(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}