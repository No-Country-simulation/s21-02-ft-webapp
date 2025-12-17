package com.wallex.financial_platform.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "suggested_reserve")
@AllArgsConstructor
@NoArgsConstructor
public class SuggestedReserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "suggested_reserve_id")
    private Long suggestedReserveId;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(name = "icon_url", length = 500)
    private String iconUrl;
}
