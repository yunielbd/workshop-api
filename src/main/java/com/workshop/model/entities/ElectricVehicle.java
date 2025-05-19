package com.workshop.model.entities;

import com.workshop.model.enums.BatteryType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ElectricVehicle extends Vehicle {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BatteryType batteryType;

    @Column(nullable = false)
    private double voltage;

    @Column(nullable = false)
    private double current;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "conversion_id")
    private Conversion conversion;
}
