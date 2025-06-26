package com.project.tfms.dto;

public class TripSummaryDto {

    private Long vehicleId;
    private String registrationNumber;
    private long totalTrips;
    private long totalTripDurationMinutes;

    public TripSummaryDto(Long vehicleId, String registrationNumber, long totalTrips, long totalTripDurationMinutes) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.totalTrips = totalTrips;
        this.totalTripDurationMinutes = totalTripDurationMinutes;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public long getTotalTrips() {
        return totalTrips;
    }

    public long getTotalTripDurationMinutes() {
        return totalTripDurationMinutes;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setTotalTrips(long totalTrips) {
        this.totalTrips = totalTrips;
    }

    public void setTotalTripDurationMinutes(long totalTripDurationMinutes) {
        this.totalTripDurationMinutes = totalTripDurationMinutes;
    }
}