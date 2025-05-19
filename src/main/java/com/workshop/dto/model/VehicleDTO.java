package com.workshop.dto.model;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class VehicleDTO {
    private UUID id;
    private String type;
    private String vin;
    private String licensePlate;
}
