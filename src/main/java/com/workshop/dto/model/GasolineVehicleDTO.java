// GasolineVehicleDTO.java
package com.workshop.dto.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
public class GasolineVehicleDTO extends VehicleDTO {
//    private String licensePlate;
    private Set<String> fuelTypes;
    private boolean wasConverted;
}
