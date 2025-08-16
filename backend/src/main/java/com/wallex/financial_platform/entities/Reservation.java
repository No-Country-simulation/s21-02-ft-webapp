package com.wallex.financial_platform.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.wallex.financial_platform.entities.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@Table(name = "reservations")
@AllArgsConstructor @NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long reservationId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "account_id", nullable = false)
    @JsonBackReference
    private Account account;

    @Column( nullable = false)
    private BigDecimal reservedAmount;

    @Column( nullable = false)
    private LocalDateTime creationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reservation_type_id", nullable = false)
    @JsonManagedReference
    private ReservationType reservationType;

    @PrePersist
    protected void onCreate() {
        creationDate = LocalDateTime.now();
    };
}
