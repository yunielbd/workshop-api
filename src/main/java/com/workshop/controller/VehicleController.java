package com.workshop.controller;

import com.workshop.dto.exception.ValidationErrorResponseDTO;
import com.workshop.dto.model.CreateVehicleRequest;
import com.workshop.dto.model.RegistrationInfoDTO;
import com.workshop.dto.model.VehicleDTO;
import com.workshop.model.enums.VehicleType;
import com.workshop.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Vehicles", description = "Manage the workshop vehicle inventory")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService service;

    public VehicleController(VehicleService service) {
        this.service = service;
    }

    @Operation(summary = "List all vehicles in workshop",
            description = "Retrieve all vehicles that are currently marked as in the workshop.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list")
    })
    @GetMapping("/all")
    public List<VehicleDTO> listAll() {
        return service.getInventory();
    }

    @Operation(summary = "List vehicles by type",
            description = "Retrieve vehicles filtered by their current type (DIESEL, ELECTRIC, GASOLINE).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved filtered list"),
            @ApiResponse(responseCode = "400", description = "Invalid vehicle type supplied")
    })
    @GetMapping(params = "type")
    public List<VehicleDTO> listByType(
            @Parameter(description = "Type of vehicle to filter by", example = "ELECTRIC", required = true)
            @RequestParam VehicleType type
    ) {
        return service.getByType(type);
    }

    @Operation(summary = "Register a new vehicle",
            description = "Add a new vehicle to the workshop inventory. The request payload must match the vehicle type.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vehicle created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or duplicate entry"),
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VehicleDTO create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Payload for creating a vehicle", required = true)
            @RequestBody
            @Valid
            CreateVehicleRequest req
    ) {
        return service.registerNew(req);
    }

    @Operation(summary = "Check out (remove) a vehicle",
            description = "Mark a vehicle as checked out (no longer in the workshop) by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehicle checked out successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @DeleteMapping("/checkout/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void checkOut(
            @Parameter(description = "UUID of the vehicle to check out", required = true)
            @PathVariable UUID id
    ) {
//        service.checkOut(id);
        service.deleteVehicle(id);
    }

    @Operation(summary = "Get encoded registration info for all vehicles")
    @GetMapping("/registration-info")
    public List<RegistrationInfoDTO> registrationInfo() {
        return service.getRegistrationInfo();
    }


    @Operation(
            summary = "Remove a vehicle",
            description = "Delete a vehicle from the inventory by its ID."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vehicle removed successfully"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponseDTO.class)))
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVehicle(
            @Parameter(description = "UUID of the vehicle to remove", required = true)
            @PathVariable UUID id
    ) {
        service.deleteVehicle(id);
    }

    @Operation(
            summary     = "Get registration info for one vehicle",
            description = "Returns the encoded registrationInfo and conversionInfo for the specified vehicle."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved registration info"),
            @ApiResponse(responseCode = "404", description = "Vehicle not found")
    })
    @GetMapping("/{id}/registration-info")
    public RegistrationInfoDTO getRegistrationInfo(
            @Parameter(description = "UUID of the vehicle", required = true)
            @PathVariable UUID id
    ) {
        return service.getRegistrationInfoById(id);
    }
}
