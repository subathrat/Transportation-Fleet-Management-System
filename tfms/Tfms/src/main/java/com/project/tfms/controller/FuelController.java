package com.project.tfms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.tfms.model.Fuel;
import com.project.tfms.model.Vehicle;
import com.project.tfms.service.FuelService;
import com.project.tfms.service.VehicleService;
import com.project.tfms.dto.FuelUsageDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class FuelController {

    @Autowired
    private FuelService fuelService;

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/fuel")
    public String showFuelForm(Model model) {
        model.addAttribute("fuel", new Fuel());
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        model.addAttribute("vehicles", vehicles);
        return "index";
    }

    @PostMapping("/fuel/add")
    public String addFuelRecord(@ModelAttribute("fuel") Fuel fuel, Model model) {
        // Validate if the selected vehicle ID exists
        if (!vehicleService.isRegisteredVehicleId(fuel.getVehicleId())) {
            model.addAttribute("error", "Vehicle ID not found. Please select a registered vehicle.");
            model.addAttribute("fuel", fuel);
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "index";
        }

        fuelService.addFuelRecord(fuel);
        return "redirect:/usage";
    }

    @GetMapping("/usage")
    public String viewFuelUsage(Model model) {
        List<Fuel> fuelRecords = fuelService.getAllFuelRecords();
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        Map<Long, String> vehicleRegistrationMap = vehicles.stream()
                .collect(Collectors.toMap(Vehicle::getVehicleId, Vehicle::getRegistrationNumber));

        List<FuelUsageDto> fuelUsageDtos = fuelRecords.stream()
                .map(fuel -> new FuelUsageDto(
                        fuel.getFuelId(),
                        fuel.getVehicleId(),
                        vehicleRegistrationMap.getOrDefault(fuel.getVehicleId(), "N/A"), // Get reg number, or "N/A" if not found
                        fuel.getDate(),
                        fuel.getFuelQuantity(),
                        fuel.getCost()
                ))
                .collect(Collectors.toList());

        model.addAttribute("fuelRecords", fuelUsageDtos);
        return "usage";
    }

    @PostMapping("/fuel/delete")
    public String deleteFuelRecord(@RequestParam("fuelId") Long fuelId, RedirectAttributes redirectAttributes) {
        try {
            fuelService.deleteFuelRecord(fuelId);
            redirectAttributes.addFlashAttribute("successMessage", "Fuel record deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting fuel record: " + e.getMessage());
        }
        return "redirect:/usage";
    }
}