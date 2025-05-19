package com.workshop.service;

import com.workshop.dto.model.RegistrationInfoDTO;
import com.workshop.dto.model.VehicleDTO;
import com.workshop.mapper.VehicleMapper;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import com.workshop.dto.model.CreateVehicleRequest;
import com.workshop.exception.NotFoundException;
import com.workshop.model.entities.*;
import com.workshop.model.enums.VehicleType;
import com.workshop.repository.VehicleRepository;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class VehicleService {
    private final Validator validator;
    private final VehicleRepository vehicleRepo;
    private final VehicleMapper mapper;
    // ya no inyectamos mapper

    public VehicleService(Validator validator, VehicleRepository vehicleRepo, VehicleMapper mapper) {
        this.validator = validator;
        this.vehicleRepo = vehicleRepo;
        this.mapper = mapper;
    }

    public List<VehicleDTO> getInventory() {
//        return vehicleRepo.findByInWorkshopTrue()
        return vehicleRepo.findAll()
                .stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<VehicleDTO> getByType(VehicleType type) {
        return vehicleRepo.findByType(type).stream()
                .map(mapper::toDto)
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

        if (req.getConversionFuelTypes() != null && !req.getConversionFuelTypes().isEmpty()) {
            if (req.getType() != VehicleType.ELECTRIC) {
                throw new IllegalArgumentException("Only electric vehicles can have a conversion");
            }
        }

        Vehicle entity = mapper.fromCreateRequest(req);

        if (entity instanceof ElectricVehicle ev
                && req.getConversionFuelTypes() != null
                && !req.getConversionFuelTypes().isEmpty())
        {
            Conversion conv = new Conversion();
            conv.setConvertedFuelTypes(req.getConversionFuelTypes());
            ev.setConversion(conv);
        }

        Vehicle saved  = vehicleRepo.save(entity);
        return mapper.toDto(saved);
    }

//    public void checkOut(UUID id) {
//        Vehicle v = vehicleRepo.findById(id)
//                .orElseThrow(() -> new NotFoundException("Vehicle not found"));
//        v.setInWorkshop(false);
//        vehicleRepo.save(v);
//    }

    public void deleteVehicle(UUID id) {
        if (!vehicleRepo.existsById(id)) {
            throw new NotFoundException("Vehicle not found");
        }
        vehicleRepo.deleteById(id);
    }

    /** Lista la información codificada de registro y conversión */
    public List<RegistrationInfoDTO> getRegistrationInfo() {
        return vehicleRepo.findAll()
                .stream()
                .map(mapper::toRegistrationInfoDto)
                .toList();
    }
    /** Información codificada de registro para un vehículo concreto */
    public RegistrationInfoDTO getRegistrationInfoById(UUID id) {
        Vehicle v = vehicleRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Vehicle not found: " + id));
        return mapper.toRegistrationInfoDto(v);
    }

}


