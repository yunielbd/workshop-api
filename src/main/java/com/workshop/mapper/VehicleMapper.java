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

    ElectricVehicleDTO toElectricDto(ElectricVehicle v);

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
}