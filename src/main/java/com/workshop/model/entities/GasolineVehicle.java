package com.workshop.model.entities;


import com.workshop.model.enums.FuelType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
public class GasolineVehicle extends Vehicle {

    @ElementCollection(targetClass = FuelType.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "gasoline_vehicle_fuels", joinColumns = @JoinColumn(name = "vehicle_id"))
    @Column(name = "fuel_type")
    private Set<FuelType> fuelTypes;
}

