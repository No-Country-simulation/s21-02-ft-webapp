package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.requests.ReservationTypeRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;
import com.wallex.financial_platform.dtos.responses.ReservationTypeResponseDTO;
import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.ReservationType;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
import com.wallex.financial_platform.exceptions.account.AccountErrorException;
import com.wallex.financial_platform.exceptions.account.AccountNotFoundException;
import com.wallex.financial_platform.exceptions.reservation.ReservationNotFoundException;
import com.wallex.financial_platform.exceptions.transaction.InsufficientFundsException;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.ReservationRepository;
import com.wallex.financial_platform.repositories.ReservationTypeRepository;
import com.wallex.financial_platform.services.IReservationService;
import com.wallex.financial_platform.services.api.ImageService;
import com.wallex.financial_platform.services.utils.UserContextService;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final ReservationTypeRepository reservationTypeRepository;
    private final ImageService imageService;

    @Transactional
    @Override
    public void createReservation(Long accountId, ReservationRequestDTO reservationRequestDTO) {
        Account account = this.validateAndGetAccount(accountId);
        this.validateAccountOwnership(account);
        this.validateSufficientFunds(account, reservationRequestDTO.reservedAmount());

        Reservation reservation = this.findExistingReservation(accountId, reservationRequestDTO.type())
                .map(res -> this.updateReservationAmount(res, reservationRequestDTO.reservedAmount()))
                .orElseGet(() -> this.createNewReservation(account, reservationRequestDTO));

        this.updateAccountBalance(account, reservationRequestDTO.reservedAmount().negate());
        reservation = this.saveReservation(reservation);

        mapToDTO(reservation);
    }

    @Transactional
    @Override
    public ReservationResponseDTO releaseReservation(Long reservationId, Long accountId) {
        Account account = this.validateAndGetAccount(accountId);
        this.validateAccountOwnership(account);

        Reservation reservation = this.validateReservationBelongsToAccount(reservationId, accountId);
        this.validateReservationStatus(reservation);

        this.updateAccountBalance(account, reservation.getReservedAmount());
        reservation.setStatus(ReservationStatus.RELEASED);
        reservation = this.saveReservation(reservation);

        return mapToDTO(reservation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getActiveReservationsByAccount(Long accountId) {
        Account account = this.validateAndGetAccount(accountId);
        this.validateAccountOwnership(account);

        return mapReservationsToDTOs(this.findActiveReservationsByAccount(accountId));
    }

    @Override
    public ReservationTypeResponseDTO createTypeReservation(ReservationTypeRequestDTO reservationTypeRequestDTO) {
        validateNameUnique(reservationTypeRequestDTO.name());
        String iconUrl = uploadIcon(reservationTypeRequestDTO.icon());
        ReservationType entity = buildReservationTypeEntity(reservationTypeRequestDTO.name(), iconUrl);

        ReservationType saved = reservationTypeRepository.save(entity);

        return new ReservationTypeResponseDTO(saved.getReservationTypeId(), saved.getName(), saved.getIconUrl());
    }

    @Override
    public List<ReservationTypeResponseDTO> getAllReservationTypes() {
        List<ReservationType> types = reservationTypeRepository.findAll();

        return types.stream()
                .map(type -> new ReservationTypeResponseDTO(type.getReservationTypeId(), type.getName(), type.getIconUrl()))
                .collect(Collectors.toList());
    }

    // ========== Métodos de Validación ==========

    private Account validateAndGetAccount(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Cuenta no encontrada"));
    }

    private void validateAccountOwnership(Account account) {
        User authenticatedUser = userContextService.getAuthenticatedUser();
        if (!account.getUser().getId().equals(authenticatedUser.getId())) {
            throw new AccountErrorException("No estás autorizado para operar esta cuenta.");
        }
    }

    private void validateSufficientFunds(Account account, BigDecimal amount) {
        if (account.getAvailableBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fondos insuficientes para crear la reserva");
        }
    }

    private Reservation validateReservationBelongsToAccount(Long reservationId, Long accountId) {
        Reservation reservation = this.validateAndGetReservation(reservationId);
        if (!reservation.getAccount().getAccountId().equals(accountId)) {
            throw new ReservationNotFoundException("La reserva no pertenece a la cuenta especificada");
        }
        return reservation;
    }

    private void validateReservationStatus(Reservation reservation) {
        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new IllegalStateException("La reserva no está activa");
        }
    }

    private Reservation validateAndGetReservation(Long reservationId) {
        return this.reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("Reserva no encontrada"));
    }

    private void validateNameUnique(@NotBlank(message = "El nombre es obligatorio") String name) {
        if (reservationTypeRepository.existsByName(name)) {
            throw new IllegalArgumentException("El tipo de reserva ya existe");
        }
    }

    // ========== Métodos de Búsqueda ==========

    private Optional<Reservation> findExistingReservation(Long accountId, ReservationType type) {
        ReservationType reservationType = reservationTypeRepository.findById(type.getReservationTypeId())
                .orElseThrow(() -> new IllegalArgumentException("Tipo de reserva no encontrado"));

        return reservationRepository.findByAccount_AccountIdAndReservationTypeAndStatus(accountId, reservationType, ReservationStatus.ACTIVE);
    }

    private List<Reservation> findActiveReservationsByAccount(Long accountId) {
        return reservationRepository.findByAccount_AccountIdAndStatus(accountId, ReservationStatus.ACTIVE);
    }

    // ========== Métodos de Creación y Actualización ==========

    private Reservation createNewReservation(Account account, ReservationRequestDTO reservationRequestDTO) {
        return Reservation.builder()
                .account(account)
                .reservedAmount(reservationRequestDTO.reservedAmount())
                .status(ReservationStatus.ACTIVE)
                .reservationType(reservationRequestDTO.type())
                .build();
    }

    private Reservation updateReservationAmount(Reservation reservation, BigDecimal amount) {
        reservation.setReservedAmount(reservation.getReservedAmount().add(amount));
        return reservation;
    }

    private void updateAccountBalance(Account account, BigDecimal amount) {
        account.setAvailableBalance(account.getAvailableBalance().add(amount));
        accountRepository.save(account);
    }

    private Reservation saveReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    private String uploadIcon(MultipartFile icon) {
        if (icon != null && !icon.isEmpty()) {
            return imageService.uploadImage(icon);
        }
        return null;
    }

    private ReservationType buildReservationTypeEntity(String name, String iconUrl) {
        ReservationType reservationType = new ReservationType();
        reservationType.setName(name);
        reservationType.setIconUrl(iconUrl);
        return reservationType;
    }

    // ========== Métodos de Mapeo ==========

    private ReservationResponseDTO mapToDTO(Reservation reservation) {
        return new ReservationResponseDTO(
                reservation.getReservationId(),
                reservation.getAccount().getAccountId(),
                reservation.getReservedAmount(),
                reservation.getCreationDate(),
                reservation.getStatus(),
                reservation.getReservationType()
        );
    }

    private List<ReservationResponseDTO> mapReservationsToDTOs(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}