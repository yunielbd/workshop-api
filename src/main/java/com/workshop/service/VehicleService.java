package com.workshop.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import com.workshop.dto.CreateVehicleRequest;
import com.workshop.dto.*;
import com.workshop.exception.NotFoundException;
import com.workshop.model.entities.*;
import com.workshop.model.enums.VehicleType;
import com.workshop.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;


import java.util.List;
import java.util.UUID;

import static com.workshop.model.enums.VehicleType.*;

@Service
public class VehicleService {
    private final Validator validator;
    private final VehicleRepository vehicleRepo;
    // ya no inyectamos mapper

    public VehicleService(Validator validator, VehicleRepository vehicleRepo) {
        this.validator = validator;
        this.vehicleRepo = vehicleRepo;
    }

    public List<VehicleDTO> getInventory() {
        return vehicleRepo.findByInWorkshopTrue().stream()
                .map(this::toDto)
                .toList();
    }

    public List<VehicleDTO> getByType(VehicleType type) {
        return vehicleRepo.findByType(type).stream()
                .map(this::toDto)
                .toList();
    }

    public VehicleDTO registerNew(CreateVehicleRequest req) {
        Class<?> group = switch(req.getType()) {
            case DIESEL   -> CreateVehicleRequest.DIESEL.class;
            case ELECTRIC -> CreateVehicleRequest.ELECTRIC.class;
            case GASOLINE -> CreateVehicleRequest.GAS.class;
        };
        var violations = validator.validate(req, group);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        Vehicle entity = fromRequest(req);
        // validaciones duplicados...
        Vehicle saved = vehicleRepo.save(entity);
        return toDto(saved);
    }

    public void checkOut(UUID id) {
        Vehicle v = vehicleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found"));
        v.setInWorkshop(false);
        vehicleRepo.save(v);
    }

    // ——— Conversión entidad → DTO ———
    private VehicleDTO toDto(Vehicle v) {
        VehicleDTO dto;
        if (v instanceof DieselVehicle d) {
            DieselVehicleDTO dd = new DieselVehicleDTO();
            dd.setId(d.getId());
            dd.setType(d.getType().name());
            dd.setLicensePlate(d.getLicensePlate());
            dd.setInjectionPumpType(d.getInjectionPumpType().name());
            dto = dd;

        } else if (v instanceof ElectricVehicle e) {
            ElectricVehicleDTO ed = new ElectricVehicleDTO();
            ed.setId(e.getId());
            ed.setType(e.getType().name());
            ed.setVin(e.getVin());
            ed.setBatteryType(e.getBatteryType().name());
            ed.setVoltage(e.getVoltage());
            ed.setCurrent(e.getCurrent());
            dto = ed;

        } else if (v instanceof GasolineVehicle g) {
            GasolineVehicleDTO gd = new GasolineVehicleDTO();
            gd.setId(g.getId());
            gd.setType(g.getType().name());
            gd.setLicensePlate(g.getLicensePlate());
            // pasamos enums a strings
            gd.setFuelTypes(
                    g.getFuelTypes().stream()
                            .map(Enum::name)
                            .collect(Collectors.toSet())
            );
            // opcional: si quisieras un flag de conversión:
            gd.setWasConverted(false);
            dto = gd;

        } else {
            throw new IllegalArgumentException("Unknown subtype: " + v.getClass());
        }
        return dto;
    }

    // ——— Conversión DTO petición → Entidad ———
    private Vehicle fromRequest(CreateVehicleRequest req) {
        return switch(req.getType()) {
            case DIESEL -> {
                DieselVehicle d = new DieselVehicle();
                d.setLicensePlate(req.getLicensePlate());
                d.setVin(req.getVin()); // si es necesario
                d.setType(VehicleType.DIESEL);
                d.setInjectionPumpType(req.getInjectionPumpType());
                d.setInWorkshop(true);
                yield d;
            }
            case ELECTRIC -> {
                ElectricVehicle e = new ElectricVehicle();
                e.setLicensePlate(req.getLicensePlate());
                e.setVin(req.getVin());
                e.setType(VehicleType.ELECTRIC);
                e.setBatteryType(req.getBatteryType());
                e.setVoltage(req.getVoltage());
                e.setCurrent(req.getCurrent());
                e.setInWorkshop(true);
                yield e;
            }
            case GASOLINE -> {
                GasolineVehicle g = new GasolineVehicle();
                g.setLicensePlate(req.getLicensePlate());
                g.setVin(req.getVin());
                g.setType(GASOLINE);
                g.setFuelTypes(req.getFuelTypes());
                g.setInWorkshop(true);
                yield g;
            }
        };
    }
}


