package com.workshop.repository;

import com.workshop.model.entities.Vehicle;
import com.workshop.model.enums.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);
    Optional<Vehicle> findByVin(String vin);

    // List all vehicles still in workshop
//    List<Vehicle> findByInWorkshopTrue();

    // List by type
    List<Vehicle> findByType(VehicleType type);

    // Check duplicates
    boolean existsByLicensePlate(String licensePlate);
    boolean existsByVin(String vin);
}
