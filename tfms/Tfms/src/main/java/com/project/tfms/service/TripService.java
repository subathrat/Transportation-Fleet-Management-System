package com.project.tfms.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.tfms.model.Trip;
import com.project.tfms.repository.TripRepository;
import com.project.tfms.repository.VehicleRepository;

import jakarta.transaction.Transactional;



import java.time.LocalDateTime;
import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;
    
    @Autowired
    private VehicleRepository vehicleRepository;

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public Trip getTripById(Long id) {
        return tripRepository.findById(id).orElseThrow(() -> new RuntimeException("Trip not found"));
    }

    public boolean isDriverOrVehicleAssigned(Long driverId, Long vehicleId, LocalDateTime startTime, LocalDateTime endTime, Long tripId) {
        List<Trip> trips = tripRepository.findTripsByDriverIdOrVehicleIdAndTimeRange(driverId, vehicleId, startTime, endTime, tripId);
        return !trips.isEmpty();
    }

    

    
    @Transactional
    public void saveTrip(Trip trip) {
        if (trip.getTripId() == null) {
            // New trip validations
            validateTrip(trip, null);
        } else {
            // Existing trip validations
            validateTrip(trip, trip.getTripId());
        }

        tripRepository.save(trip);
    }

    private void validateTrip(Trip trip, Long currentTripId) {
        if (trip.getEndTime().isBefore(trip.getStartTime()) || trip.getEndTime().isEqual(trip.getStartTime())) {
            throw new RuntimeException("End time must be greater than start time.");
        }

        if (trip.getStartLocation().trim().equalsIgnoreCase(trip.getEndLocation().trim())) {
            throw new RuntimeException("Start location and End location must be different.");
        }

        if (!vehicleRepository.existsById(trip.getVehicleId())) {
            throw new RuntimeException("Vehicle ID " + trip.getVehicleId() + " is not registered.");
        }

        if (isDriverOrVehicleAssigned(trip.getDriverId(), trip.getVehicleId(), trip.getStartTime(), trip.getEndTime(), currentTripId)) {
            throw new RuntimeException("Driver or vehicle is already assigned to another trip during this time.");
        }
    }


    @Transactional
    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}
