package com.project.tfms.model;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity 
public class Trip { 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "trip_id")
	private Long tripId;
private Long vehicleId;
private Long driverId;

private String startLocation;
private String endLocation;

@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
private LocalDateTime startTime;

@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
private LocalDateTime endTime;

public Long getTripId() 
{
	return tripId;
}
public void setTripId(Long tripId) { 
	this.tripId = tripId; 
	}
public Long getVehicleId() {
	return vehicleId; 
	}
public void setVehicleId(Long vehicleId) { 
	this.vehicleId = vehicleId;
	}
public Long getDriverId() {
	return driverId; 
	}
public void setDriverId(Long driverId) { 
	this.driverId = driverId;
	}
public String getStartLocation() { 
	return startLocation; 
	}
public void setStartLocation(String startLocation) {
	this.startLocation = startLocation;
	}
public String getEndLocation() {
	return endLocation; 
	}
public void setEndLocation(String endLocation) { 
	this.endLocation = endLocation; 
	}
public LocalDateTime getStartTime() {
	return startTime; 
	}
public void setStartTime(LocalDateTime startTime) { 
	this.startTime = startTime;
	}
public LocalDateTime getEndTime() { 
	return endTime; 
	}
public void setEndTime(LocalDateTime endTime) { 
	this.endTime = endTime; 
	}

}