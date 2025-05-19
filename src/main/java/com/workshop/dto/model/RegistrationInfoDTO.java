package com.workshop.dto.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.workshop.model.enums.VehicleType;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RegistrationInfoDTO {
    private UUID id;
    private VehicleType type;
    private String registrationInfo;
    private String conversionInfo;
}
