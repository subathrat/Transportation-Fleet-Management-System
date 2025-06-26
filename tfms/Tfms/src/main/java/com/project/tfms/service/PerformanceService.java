package com.project.tfms.service;

import com.project.tfms.dto.FuelUsageDto;
import com.project.tfms.dto.MaintenanceCostDto;
import com.project.tfms.dto.TripSummaryDto;
import com.project.tfms.dto.VehiclePerformanceDto;
import com.project.tfms.model.Fuel;
import com.project.tfms.model.Maintenance;
import com.project.tfms.model.Trip;
import com.project.tfms.model.Vehicle;
import com.project.tfms.repository.FuelRepository;
import com.project.tfms.repository.MaintenanceRepository;
import com.project.tfms.repository.TripRepository;
import com.project.tfms.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class PerformanceService {

    @Autowired
    private FuelRepository fuelRepository;
    @Autowired
    private VehicleRepository vehicleRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private MaintenanceRepository maintenanceRepository;


    public List<FuelUsageDto> getFuelEfficiencyData() {
        List<Fuel> fuelRecords = fuelRepository.findAll();
        List<Vehicle> vehicles = vehicleRepository.findAll();

        Map<Long, String> vehicleRegistrationMap = vehicles.stream()
                .collect(Collectors.toMap(Vehicle::getVehicleId, Vehicle::getRegistrationNumber));

        return fuelRecords.stream()
                .map(fuel -> new FuelUsageDto(
                        fuel.getFuelId(),
                        fuel.getVehicleId(),
                        vehicleRegistrationMap.getOrDefault(fuel.getVehicleId(), "N/A"),
                        fuel.getDate(),
                        fuel.getFuelQuantity(),
                        fuel.getCost()
                ))
                .collect(Collectors.toList());
    }

    public List<VehiclePerformanceDto> getOverallVehiclePerformanceData() {
        List<VehiclePerformanceDto> performanceData = new ArrayList<>();

        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Fuel> fuelRecords = fuelRepository.findAll();
        List<Trip> tripRecords = tripRepository.findAll();
        List<Maintenance> maintenanceRecords = maintenanceRepository.findAll();

        Map<Long, String> vehicleRegistrationMap = vehicles.stream()
                .collect(Collectors.toMap(Vehicle::getVehicleId, Vehicle::getRegistrationNumber));
        Map<Long, List<Fuel>> fuelByVehicleId = fuelRecords.stream()
                .collect(Collectors.groupingBy(Fuel::getVehicleId));
        Map<Long, List<Trip>> tripsByVehicleId = tripRecords.stream()
                .collect(Collectors.groupingBy(Trip::getVehicleId));
        Map<Long, List<Maintenance>> maintenanceByVehicleId = maintenanceRecords.stream()
                .collect(Collectors.groupingBy(Maintenance::getVehicleId));

        for (Vehicle vehicle : vehicles) {
            double totalFuelQuantity = 0.0;
            double totalFuelCost = 0.0;
            double averageCostPerLiter = 0.0;
            long totalTrips = 0;
            long totalTripDurationMinutes = 0;
            double totalMaintenanceCost = 0.0;

            List<Fuel> vehicleFuelRecords = fuelByVehicleId.getOrDefault(vehicle.getVehicleId(), new ArrayList<>());
            for (Fuel fuel : vehicleFuelRecords) {
                totalFuelQuantity += fuel.getFuelQuantity();
                totalFuelCost += fuel.getCost();
            }
            if (totalFuelQuantity > 0) {
                averageCostPerLiter = totalFuelCost / totalFuelQuantity;
            }

            List<Trip> vehicleTripRecords = tripsByVehicleId.getOrDefault(vehicle.getVehicleId(), new ArrayList<>());
            totalTrips = vehicleTripRecords.size();
            for (Trip trip : vehicleTripRecords) {
                if (trip.getStartTime() != null && trip.getEndTime() != null) {
                    totalTripDurationMinutes += Duration.between(trip.getStartTime(), trip.getEndTime()).toMinutes();
                }
            }

            List<Maintenance> vehicleMaintenanceRecords = maintenanceByVehicleId.getOrDefault(vehicle.getVehicleId(), new ArrayList<>());
            for (Maintenance maintenance : vehicleMaintenanceRecords) {
                if (maintenance.getCost() != null) {
                    totalMaintenanceCost += maintenance.getCost();
                }
            }

            performanceData.add(new VehiclePerformanceDto(
                    vehicle.getVehicleId(),
                    vehicle.getRegistrationNumber(),
                    totalFuelQuantity,
                    totalFuelCost,
                    averageCostPerLiter,
                    totalTrips,
                    totalTripDurationMinutes,
                    totalMaintenanceCost
            ));
        }

        return performanceData;
    }


    public List<TripSummaryDto> getTripSummaryData() {
        List<TripSummaryDto> tripSummaries = new ArrayList<>();
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Trip> tripRecords = tripRepository.findAll();

        Map<Long, List<Trip>> tripsByVehicleId = tripRecords.stream()
                .collect(Collectors.groupingBy(Trip::getVehicleId));

        for (Vehicle vehicle : vehicles) {
            List<Trip> vehicleTrips = tripsByVehicleId.getOrDefault(vehicle.getVehicleId(), new ArrayList<>());
            long totalTrips = vehicleTrips.size();
            long totalDurationMinutes = 0;
            for (Trip trip : vehicleTrips) {
                if (trip.getStartTime() != null && trip.getEndTime() != null) {
                    totalDurationMinutes += Duration.between(trip.getStartTime(), trip.getEndTime()).toMinutes();
                }
            }
            tripSummaries.add(new TripSummaryDto(
                    vehicle.getVehicleId(),
                    vehicle.getRegistrationNumber(),
                    totalTrips,
                    totalDurationMinutes
            ));
        }
        return tripSummaries;
    }

    public List<MaintenanceCostDto> getMaintenanceCostData() {
        List<MaintenanceCostDto> maintenanceCosts = new ArrayList<>();
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<Maintenance> maintenanceRecords = maintenanceRepository.findAll();

        Map<Long, List<Maintenance>> maintenanceByVehicleId = maintenanceRecords.stream()
                .collect(Collectors.groupingBy(Maintenance::getVehicleId));

        for (Vehicle vehicle : vehicles) {
            List<Maintenance> vehicleMaintenances = maintenanceByVehicleId.getOrDefault(vehicle.getVehicleId(), new ArrayList<>());
            double totalCost = vehicleMaintenances.stream()
                    .filter(m -> m.getCost() != null)
                    .mapToDouble(Maintenance::getCost)
                    .sum();
            maintenanceCosts.add(new MaintenanceCostDto(
                    vehicle.getVehicleId(),
                    vehicle.getRegistrationNumber(),
                    totalCost
            ));
        }
        return maintenanceCosts;
    }

    public ByteArrayOutputStream generateOverallPerformanceExcelReport() {
        // Retrieve the data to be put into the Excel report
        List<VehiclePerformanceDto> performanceData = getOverallVehiclePerformanceData();

        try (Workbook workbook = new XSSFWorkbook(); // Create a new XLSX workbook
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Overall Vehicle Performance");

            // Create the header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                    "Vehicle ID", "Registration Number", "Total Fuel Quantity",
                    "Total Fuel Cost", "Avg Cost Per Liter", "Total Trips",
                    "Total Trip Duration (Minutes)", "Total Maintenance Cost"
            };
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // Populate data rows
            int rowNum = 1;
            for (VehiclePerformanceDto dto : performanceData) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(dto.getVehicleId());
                row.createCell(1).setCellValue(dto.getRegistrationNumber());
                row.createCell(2).setCellValue(dto.getTotalFuelQuantity());
                row.createCell(3).setCellValue(dto.getTotalFuelCost());
                row.createCell(4).setCellValue(dto.getAverageCostPerLiter());
                row.createCell(5).setCellValue(dto.getTotalTrips());
                row.createCell(6).setCellValue(dto.getTotalTripDurationMinutes());
                row.createCell(7).setCellValue(dto.getTotalMaintenanceCost());
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return outputStream;

        } catch (IOException e) {
            throw new RuntimeException("Failed to generate Excel report: " + e.getMessage(), e);
        }
    }
}