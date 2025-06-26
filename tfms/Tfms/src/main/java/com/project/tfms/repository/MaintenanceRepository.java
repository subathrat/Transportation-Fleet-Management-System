
package com.project.tfms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.tfms.model.Maintenance;

import java.time.LocalDate;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    boolean existsByVehicleIdAndDescriptionAndScheduledDate(Long vehicleId, String description, LocalDate scheduledDate);
}