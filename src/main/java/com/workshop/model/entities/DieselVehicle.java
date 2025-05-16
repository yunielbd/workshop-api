package com.workshop.model.entities;


import com.workshop.model.enums.InjectionPumpType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class DieselVehicle extends Vehicle {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InjectionPumpType injectionPumpType;
}

