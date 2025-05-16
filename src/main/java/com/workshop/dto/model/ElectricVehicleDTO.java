// ElectricVehicleDTO.java
package com.workshop.dto.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ElectricVehicleDTO extends VehicleDTO {
    private double voltage;
    private double current;
    private String batteryType;
}
