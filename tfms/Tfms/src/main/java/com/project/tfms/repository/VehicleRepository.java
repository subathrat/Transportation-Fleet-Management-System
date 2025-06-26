package com.project.tfms.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.tfms.model.Vehicle;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
	Optional<Vehicle> findFirstByRegistrationNumber(String registrationNumber);

}
