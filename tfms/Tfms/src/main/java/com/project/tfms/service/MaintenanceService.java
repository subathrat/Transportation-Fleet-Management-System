
package com.project.tfms.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.tfms.model.Maintenance;
import com.project.tfms.repository.MaintenanceRepository;
import com.project.tfms.repository.VehicleRepository;

@Service
public class MaintenanceService{

	@Autowired
	private VehicleRepository vehicleRepository;

	@Autowired
	private MaintenanceRepository maintenanceRepository;


	public List<Maintenance> getAllMaintenances() {
		return maintenanceRepository.findAll();
	}


	public Optional<Maintenance> getMaintenanceById(Long id) {
		return maintenanceRepository.findById(id);
	}

	public boolean isRegisteredVehicleId(Long vehicleId) {
		return vehicleRepository.existsById(vehicleId);
	}

	public Maintenance scheduleMaintenance(Maintenance maintenance) {
		if (maintenanceRepository.existsByVehicleIdAndDescriptionAndScheduledDate(
				maintenance.getVehicleId(),
				maintenance.getDescription(),
				maintenance.getScheduledDate())) {
			throw new IllegalArgumentException("A maintenance task with the same vehicle, description, and scheduled date already exists.");
		}
		return maintenanceRepository.save(maintenance);
	}

	public Maintenance updateMaintenance(Maintenance maintenance) {
		// When updating, we need to check if the new combination of vehicleId, description, and scheduledDate
		// already exists for *another* maintenance record (i.e., not the one being updated).
		// If the ID is null or 0, it means it's a new record, which scheduleMaintenance handles.
		if (maintenance.getMaintenanceId() != null) {
			Optional<Maintenance> existingMaintenance = maintenanceRepository.findById(maintenance.getMaintenanceId());
			if (existingMaintenance.isPresent()) {
				Maintenance currentMaintenance = existingMaintenance.get();
				// Check if the unique fields have changed. If they have and conflict with another record, throw error.
				if (!currentMaintenance.getVehicleId().equals(maintenance.getVehicleId()) ||
						!currentMaintenance.getDescription().equals(maintenance.getDescription()) ||
						!currentMaintenance.getScheduledDate().equals(maintenance.getScheduledDate())) {

					if (maintenanceRepository.existsByVehicleIdAndDescriptionAndScheduledDate(
							maintenance.getVehicleId(),
							maintenance.getDescription(),
							maintenance.getScheduledDate())) {
						throw new IllegalArgumentException("Cannot update: Another maintenance task with the same vehicle, description, and scheduled date already exists.");
					}
				}
			}
		}
		return maintenanceRepository.save(maintenance);
	}

	public void deleteMaintenance(Long id) {
		maintenanceRepository.deleteById(id);
	}
}