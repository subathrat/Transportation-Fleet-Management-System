
package com.project.tfms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.project.tfms.model.Maintenance;
import com.project.tfms.model.Vehicle;
import com.project.tfms.repository.MaintenanceRepository;
import com.project.tfms.service.VehicleService;
import com.project.tfms.service.MaintenanceService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/maintenances")
public class MaintenanceController {

    @Autowired
    private MaintenanceRepository maintenanceRepository;

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private MaintenanceService maintenanceService;

    @GetMapping
    public String listMaintenances(Model model) {
        List<Maintenance> maintenances = maintenanceService.getAllMaintenances();
        List<Vehicle> vehicles = vehicleService.getAllVehicles();

        Map<Long, String> vehicleRegistrationMap = vehicles.stream()
                .collect(Collectors.toMap(Vehicle::getVehicleId, Vehicle::getRegistrationNumber));

        model.addAttribute("maintenances", maintenances);
        model.addAttribute("vehicleRegistrationMap", vehicleRegistrationMap);
        return "maintenance-list";
    }

    @GetMapping("/new")
    public String showScheduleForm(Model model) {
        model.addAttribute("maintenance", new Maintenance());
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        model.addAttribute("vehicles", vehicles);
        return "maintenance-form";
    }

    @PostMapping("/schedule")
    public String scheduleMaintenance(@ModelAttribute Maintenance maintenance, Model model, RedirectAttributes redirectAttributes) {
        if (!vehicleService.isRegisteredVehicleId(maintenance.getVehicleId())) {
            model.addAttribute("maintenance", maintenance);
            model.addAttribute("error", "Vehicle ID is not registered. Please select a valid vehicle.");
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "maintenance-form";
        }
        try {
            maintenanceService.scheduleMaintenance(maintenance);
            redirectAttributes.addFlashAttribute("message", "Maintenance record scheduled successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (IllegalArgumentException e) {
            model.addAttribute("maintenance", maintenance);
            model.addAttribute("error", e.getMessage());
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "maintenance-form";
        } catch (Exception e) {
            model.addAttribute("maintenance", maintenance);
            model.addAttribute("error", "An unexpected error occurred while scheduling maintenance.");
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "maintenance-form";
        }
        return "redirect:/maintenances";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<Maintenance> maintenance = maintenanceService.getMaintenanceById(id);
        if (maintenance.isPresent()) {
            model.addAttribute("maintenance", maintenance.get());
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "maintenance-form";
        }
        return "redirect:/maintenances";
    }

    @PostMapping("/update")
    public String updateMaintenance(@ModelAttribute Maintenance maintenance, Model model, RedirectAttributes redirectAttributes) {
        if (!vehicleService.isRegisteredVehicleId(maintenance.getVehicleId())) {
            model.addAttribute("maintenance", maintenance);
            model.addAttribute("error", "Vehicle ID is not registered. Please select a valid vehicle.");
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "maintenance-form";
        }
        try {
            maintenanceService.updateMaintenance(maintenance);
            redirectAttributes.addFlashAttribute("message", "Maintenance record updated successfully!");
            redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        } catch (IllegalArgumentException e) {
            model.addAttribute("maintenance", maintenance);
            model.addAttribute("error", e.getMessage());
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "maintenance-form";
        } catch (Exception e) {
            model.addAttribute("maintenance", maintenance);
            model.addAttribute("error", "An unexpected error occurred while updating maintenance.");
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            model.addAttribute("vehicles", vehicles);
            return "maintenance-form";
        }
        return "redirect:/maintenances";
    }

    @GetMapping("/delete/{id}")
    public String deleteMaintenance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        maintenanceService.deleteMaintenance(id);
        redirectAttributes.addFlashAttribute("message", "Maintenance record deleted successfully!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");
        return "redirect:/maintenances";
    }
}