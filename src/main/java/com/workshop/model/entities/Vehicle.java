package com.workshop.model.entities;

import com.workshop.model.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
@NoArgsConstructor
public abstract class Vehicle {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String licensePlate;

    @Column(unique = true, nullable = false)
    private String vin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

//    @Column(nullable = false)
//    private boolean inWorkshop = true;
}
