package com.workshop.mapper;

import com.workshop.dto.model.*;
import com.workshop.model.entities.*;
import com.workshop.model.enums.VehicleType;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        imports = {
            java.util.stream.Collectors.class,
            com.workshop.model.enums.VehicleType.class
        })
public interface VehicleMapper {

    DieselVehicleDTO toDieselDto(DieselVehicle v);

    @Mapping(target = "conversionFuelTypes",
            expression = "java(e.getConversion()!=null ? "
                    + "e.getConversion().getConvertedFuelTypes().stream()"
                    +     ".map(Enum::name)"
                    +     ".collect(Collectors.toSet())"
                    + " : null)")
    ElectricVehicleDTO toElectricDto(ElectricVehicle e);

    @Mapping(target = "wasConverted", constant = "false")
    @Mapping(
            target = "fuelTypes",
            expression = "java(v.getFuelTypes().stream().map(Enum::name).collect(Collectors.toSet()))"
    )
    GasolineVehicleDTO toGasolineDto(GasolineVehicle v);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "inWorkshop", constant = "true")
    @Mapping(target = "type", source = "type") // Directly map the VehicleType
    DieselVehicle fromCreateDieselRequest(CreateVehicleRequest req);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "inWorkshop", constant = "true")
    @Mapping(target = "type", source = "type") // Directly map the VehicleType
    ElectricVehicle fromCreateElectricRequest(CreateVehicleRequest req);

    @Mapping(target = "id", ignore = true)
//    @Mapping(target = "inWorkshop", constant = "true")
    @Mapping(target = "type", source = "type") // Directly map the VehicleType
    GasolineVehicle fromCreateGasolineRequest(CreateVehicleRequest req);

    default Vehicle fromCreateRequest(CreateVehicleRequest req) {
        return switch (req.getType()) {
            case DIESEL -> fromCreateDieselRequest(req);
            case ELECTRIC -> fromCreateElectricRequest(req);
            case GASOLINE -> fromCreateGasolineRequest(req);
        };
    }

    default VehicleDTO toDto(Vehicle v) {
        if (v instanceof DieselVehicle d) return toDieselDto(d);
        if (v instanceof ElectricVehicle e) return toElectricDto(e);
        if (v instanceof GasolineVehicle g) return toGasolineDto(g);
        throw new IllegalArgumentException("Unknown subtype: " + v.getClass());
    }

    default String mapVehicleTypeToString(VehicleType type) {
        return type != null ? type.name() : null;
    }

    default VehicleType mapStringToVehicleType(String type) {
        return type != null ? VehicleType.valueOf(type) : null;
    }

    // Diesel → VehicleRegistrationInfoDTO
    @Mapping(target = "id",               source = "d.id")
//    @Mapping(target = "type",             expression = "java(d.getType().name())")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "registrationInfo", expression = "java(d.getLicensePlate() + d.getInjectionPumpType().name())")
    RegistrationInfoDTO toRegistrationInfoDto(DieselVehicle d);

    // Electric → VehicleRegistrationInfoDTO
    @Mapping(target = "id",               source = "e.id")
    @Mapping(target = "type", source = "type")
//    @Mapping(target = "type",             expression = "java(e.getType().name())")
    @Mapping(target = "registrationInfo",
            expression = "java(e.getVin() + \"V\" + e.getVoltage() + \"A\" + e.getCurrent() + e.getBatteryType().name())")
    @Mapping(target = "conversionInfo",
            expression = "java(e.getConversion() != null ? "
                    + "e.getLicensePlate() + \" \" + "
                    + "e.getConversion().getConvertedFuelTypes().stream()"
                    + ".map(Enum::name)"
                    + ".collect(Collectors.joining(\",\"))"
                    + " : null)")
    RegistrationInfoDTO toRegistrationInfoDto(ElectricVehicle e);

    // Gasoline → VehicleRegistrationInfoDTO
    @Mapping(target = "id",               source = "g.id")
//    @Mapping(target = "type",             expression = "java(g.getType().name())")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "registrationInfo",
            expression = "java(g.getLicensePlate() + " +
                    "g.getFuelTypes().stream().map(Enum::name).collect(Collectors.joining(\"-\")))")
    RegistrationInfoDTO toRegistrationInfoDto(GasolineVehicle g);

    // Polimórfico
    default RegistrationInfoDTO toRegistrationInfoDto(Vehicle v) {
        if (v instanceof DieselVehicle d)   return toRegistrationInfoDto(d);
        if (v instanceof ElectricVehicle e) return toRegistrationInfoDto(e);
        if (v instanceof GasolineVehicle g) return toRegistrationInfoDto(g);
        throw new IllegalArgumentException("Unknown subtype: " + v.getClass());
    }
}