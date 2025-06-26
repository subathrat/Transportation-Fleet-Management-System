package com.project.tfms.dto;

import java.time.LocalDateTime;

public class FuelUsageDto {

    private Long fuelId;
    private Long vehicleId;
    private String registrationNumber;
    private LocalDateTime date;
    private Double fuelQuantity;
    private Double cost;

    public FuelUsageDto() {
    }

    public FuelUsageDto(Long fuelId, Long vehicleId, String registrationNumber,
                        LocalDateTime date, Double fuelQuantity, Double cost) {
        this.fuelId = fuelId;
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.date = date;
        this.fuelQuantity = fuelQuantity;
        this.cost = cost;
    }

    public FuelUsageDto(Long vehicleId, String registrationNumber, double totalFuelQuantity, double totalCost, double averageCostPerLiter) {
    }

    // Getters
    public Long getFuelId() { return fuelId; }
    public Long getVehicleId() { return vehicleId; }
    public String getRegistrationNumber() { return registrationNumber; }
    public LocalDateTime getDate() { return date; }
    public Double getFuelQuantity() { return fuelQuantity; }
    public Double getCost() { return cost; }

    // Setters
    public void setFuelId(Long fuelId) { this.fuelId = fuelId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public void setRegistrationNumber(String registrationNumber) { this.registrationNumber = registrationNumber; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public void setFuelQuantity(Double fuelQuantity) { this.fuelQuantity = fuelQuantity; }
    public void setCost(Double cost) { this.cost = cost; }

    @Override
    public String toString() {
        return "FuelUsageDto{" +
                "fuelId=" + fuelId +
                ", vehicleId=" + vehicleId +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", date=" + date +
                ", fuelQuantity=" + fuelQuantity +
                ", cost=" + cost +
                '}';
    }
}