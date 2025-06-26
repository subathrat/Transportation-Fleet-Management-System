package com.project.tfms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.project.tfms.model.Trip;

import java.time.LocalDateTime; import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
	@Query("SELECT t FROM Trip t WHERE (t.driverId = :driverId OR t.vehicleId = :vehicleId) AND (t.startTime < :endTime AND t.endTime > :startTime) AND (t.tripId != :tripId OR :tripId IS NULL)") 
	List<Trip> findTripsByDriverIdOrVehicleIdAndTimeRange(@Param("driverId") Long driverId, @Param("vehicleId") Long vehicleId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime, @Param("tripId") Long tripId); 
	}
