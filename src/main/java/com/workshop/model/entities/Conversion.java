package com.workshop.model.entities;

import com.workshop.model.enums.FuelType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Getter @Setter @NoArgsConstructor
public class Conversion {
    @Id @GeneratedValue
    private UUID id;

    @ElementCollection(targetClass = FuelType.class)
    @CollectionTable(
            name = "conversion_fuel_types",
            joinColumns = @JoinColumn(name = "conversion_id")
    )
    @Column(name = "fuel_type")
    @Enumerated(EnumType.STRING)
    private Set<FuelType> convertedFuelTypes;
}
