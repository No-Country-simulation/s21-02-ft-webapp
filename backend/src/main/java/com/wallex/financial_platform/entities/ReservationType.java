package com.wallex.financial_platform.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "reservation_type")
@AllArgsConstructor
@NoArgsConstructor
public class ReservationType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_type_id")
    private Long reservationTypeId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    @OneToOne(mappedBy = "reservationType", fetch = FetchType.LAZY)
    @JsonBackReference
    private Reservation reservation;

}
