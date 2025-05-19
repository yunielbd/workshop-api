// DieselVehicleDTO.java
package com.workshop.dto.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class DieselVehicleDTO extends VehicleDTO {
//    private String licensePlate;
    private String injectionPumpType;
}
