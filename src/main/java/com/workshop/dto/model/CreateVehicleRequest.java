package com.workshop.dto.model;

import com.workshop.model.enums.BatteryType;
import com.workshop.model.enums.FuelType;
import com.workshop.model.enums.InjectionPumpType;
import com.workshop.model.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.Set;

@Data
public class CreateVehicleRequest {
    @NotNull
    private VehicleType type;

//    @NotBlank(groups = { GAS.class, DIESEL.class })
//    private String licensePlate;

    @NotNull
    private String licensePlate;

    @NotNull
    private String vin;

    @NotNull(groups = DIESEL.class)
    private InjectionPumpType injectionPumpType;

    @NotNull(groups = ELECTRIC.class)
    private BatteryType batteryType;
    @Positive(groups = ELECTRIC.class)
    private Double voltage;
    @Positive(groups = ELECTRIC.class)
    private Double current;

    @NotEmpty(groups = GAS.class)
    private Set<FuelType> fuelTypes;

    private Set<@NotNull FuelType> conversionFuelTypes;

    public interface DIESEL {}
    public interface ELECTRIC {}
    public interface GAS {}
}
