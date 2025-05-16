// ElectricVehicleDTO.java
package com.workshop.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ElectricVehicleDTO extends VehicleDTO {
    private String vin;
    private double voltage;
    private double current;
    private String batteryType;
}
