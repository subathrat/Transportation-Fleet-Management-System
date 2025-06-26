package com.project.tfms.dto;

public class MaintenanceCostDto {

    private Long vehicleId;
    private String registrationNumber;
    private double totalMaintenanceCost;
    public MaintenanceCostDto(Long vehicleId, String registrationNumber, double totalMaintenanceCost) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.totalMaintenanceCost = totalMaintenanceCost;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
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

    public void setTotalMaintenanceCost(double totalMaintenanceCost) {
        this.totalMaintenanceCost = totalMaintenanceCost;
    }
}