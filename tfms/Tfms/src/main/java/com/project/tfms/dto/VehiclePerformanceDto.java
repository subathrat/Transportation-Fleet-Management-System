package com.project.tfms.dto;

public class VehiclePerformanceDto {

    private Long vehicleId;
    private String registrationNumber;

    private double totalFuelQuantity;
    private double totalFuelCost;
    private double averageCostPerLiter;

 
    private long totalTrips;
    private long totalTripDurationMinutes;

  
    private double totalMaintenanceCost;

    public VehiclePerformanceDto(
            Long vehicleId,
            String registrationNumber,
            double totalFuelQuantity,
            double totalFuelCost,
            double averageCostPerLiter,
            long totalTrips,
            long totalTripDurationMinutes,
            double totalMaintenanceCost) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.totalFuelQuantity = totalFuelQuantity;
        this.totalFuelCost = totalFuelCost;
        this.averageCostPerLiter = averageCostPerLiter;
        this.totalTrips = totalTrips;
        this.totalTripDurationMinutes = totalTripDurationMinutes;
        this.totalMaintenanceCost = totalMaintenanceCost;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public double getTotalFuelQuantity() {
        return totalFuelQuantity;
    }

    public double getTotalFuelCost() {
        return totalFuelCost;
    }

    public double getAverageCostPerLiter() {
        return averageCostPerLiter;
    }

    public long getTotalTrips() {
        return totalTrips;
    }

    public long getTotalTripDurationMinutes() {
        return totalTripDurationMinutes;
    }

    public double getTotalMaintenanceCost() {
        return totalMaintenanceCost;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public void setTotalFuelQuantity(double totalFuelQuantity) {
        this.totalFuelQuantity = totalFuelQuantity;
    }

    public void setTotalFuelCost(double totalFuelCost) {
        this.totalFuelCost = totalFuelCost;
    }

    public void setAverageCostPerLiter(double averageCostPerLiter) {
        this.averageCostPerLiter = averageCostPerLiter;
    }

    public void setTotalTrips(long totalTrips) {
        this.totalTrips = totalTrips;
    }

    public void setTotalTripDurationMinutes(long totalTripDurationMinutes) {
        this.totalTripDurationMinutes = totalTripDurationMinutes;
    }

    public void setTotalMaintenanceCost(double totalMaintenanceCost) {
        this.totalMaintenanceCost = totalMaintenanceCost;
    }
}