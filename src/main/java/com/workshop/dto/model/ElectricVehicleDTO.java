// ElectricVehicleDTO.java
package com.workshop.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ElectricVehicleDTO extends VehicleDTO {
    private double voltage;
    private double current;
    private String batteryType;

    private Set<String> conversionFuelTypes;
}
