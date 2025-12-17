package com.wallex.financial_platform.services.impl;

import com.wallex.financial_platform.dtos.requests.ReservationRequestDTO;
import com.wallex.financial_platform.dtos.requests.SuggestedReserveRequestDTO;
import com.wallex.financial_platform.dtos.responses.ReservationResponseDTO;
import com.wallex.financial_platform.dtos.responses.SuggestedReserveResponseDTO;
import com.wallex.financial_platform.entities.Account;
import com.wallex.financial_platform.entities.Reservation;
import com.wallex.financial_platform.entities.SuggestedReserve;
import com.wallex.financial_platform.entities.User;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
import com.wallex.financial_platform.exceptions.account.AccountErrorException;
import com.wallex.financial_platform.exceptions.account.AccountNotFoundException;
import com.wallex.financial_platform.exceptions.reservation.ReservationNotFoundException;
import com.wallex.financial_platform.exceptions.transaction.InsufficientFundsException;
import com.wallex.financial_platform.repositories.AccountRepository;
import com.wallex.financial_platform.repositories.ReservationRepository;
import com.wallex.financial_platform.repositories.SuggestedReserveRepository;
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
    private final SuggestedReserveRepository suggestedReserveRepository;
    private final ImageService imageService;

    @Transactional
    @Override
    public void createReservation(Long accountId, ReservationRequestDTO reservationRequestDTO) {
        Account account = this.validateAndGetAccount(accountId);
        this.validateAccountOwnership(account);
        this.validateSufficientFunds(account, reservationRequestDTO.reservedAmount());

        // La lógica ha sido refactorizada para no mezclar los tipos de entidad
        Optional<Reservation> optionalReservation = findExistingActiveReservationByReason(accountId, reservationRequestDTO.reason());

        Reservation reservation;
        if (optionalReservation.isPresent()) {
            reservation = optionalReservation.get();
            reservation.setReservedAmount(reservation.getReservedAmount().add(reservationRequestDTO.reservedAmount()));
        } else {
            reservation = createNewReservation(account, reservationRequestDTO);
        }

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
    public SuggestedReserveResponseDTO createSuggestedReservations(SuggestedReserveRequestDTO reservationTypeRequestDTO) {
        validateNameUnique(reservationTypeRequestDTO.name());
        String iconUrl = uploadIcon(reservationTypeRequestDTO.icon());
        SuggestedReserve entity = buildReservationTypeEntity(reservationTypeRequestDTO.name(), iconUrl);

        SuggestedReserve saved = suggestedReserveRepository.save(entity);

        return new SuggestedReserveResponseDTO(saved.getSuggestedReserveId(), saved.getName(), saved.getIconUrl());
    }

    @Override
    public List<SuggestedReserveResponseDTO> getAllSuggestedReservations() {
        List<SuggestedReserve> types = suggestedReserveRepository.findAll();

        return types.stream()
                .map(type -> new SuggestedReserveResponseDTO(type.getSuggestedReserveId(), type.getName(), type.getIconUrl()))
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
        if (suggestedReserveRepository.existsByName(name)) {
            throw new IllegalArgumentException("El tipo de reserva ya existe");
        }
    }

    // ========== Métodos de Búsqueda ==========

    // Este es el método refactorizado. Ahora solo busca una Reservation
    private Optional<Reservation> findExistingActiveReservationByReason(Long accountId, String reason) {
        return reservationRepository.findByAccount_AccountIdAndReasonAndStatus(
                accountId, reason, ReservationStatus.ACTIVE
        );
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
                .reason(reservationRequestDTO.reason())
                .build();
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

    private SuggestedReserve buildReservationTypeEntity(String name, String iconUrl) {
        SuggestedReserve reservationType = new SuggestedReserve();
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
                reservation.getReason()
        );
    }

    private List<ReservationResponseDTO> mapReservationsToDTOs(List<Reservation> reservations) {
        return reservations.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}