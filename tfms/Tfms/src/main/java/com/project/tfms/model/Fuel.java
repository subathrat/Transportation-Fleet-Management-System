package com.project.tfms.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Fuel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fuelId;

    private Long vehicleId;
    private LocalDateTime date;
    private Double fuelQuantity;
    private Double cost;

    public Fuel() {
    }

    public Fuel(Long fuelId, Long vehicleId, LocalDateTime date, Double fuelQuantity, Double cost) {
        this.fuelId = fuelId;
        this.vehicleId = vehicleId;
        this.date = date;
        this.fuelQuantity = fuelQuantity;
        this.cost = cost;
    }

    public Long getFuelId() {
        return fuelId;
    }

    public void setFuelId(Long fuelId) {
        this.fuelId = fuelId;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Double getFuelQuantity() {
        return fuelQuantity;
    }

    public void setFuelQuantity(Double fuelQuantity) {
        this.fuelQuantity = fuelQuantity;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Fuel{" +
                "fuelId=" + fuelId +
                ", vehicleId=" + vehicleId +
                ", date=" + date +
                ", fuelQuantity=" + fuelQuantity +
                ", cost=" + cost +
                '}';
    }
}