package com.workshop.model.entities;

import com.workshop.model.enums.FuelType;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Conversion {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FuelType convertedToFuelType;
}

