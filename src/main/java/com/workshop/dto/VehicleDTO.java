package com.workshop.dto;

import lombok.Data;

import java.util.UUID;

@Data
public abstract class VehicleDTO {
    protected UUID id;
    protected String type;
}
