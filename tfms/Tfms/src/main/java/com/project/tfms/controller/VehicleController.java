package com.project.tfms.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.project.tfms.model.Vehicle;
import com.project.tfms.service.VehicleService;

@Controller
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService service;

    @GetMapping
    public String viewVehicleList(Model model) {
        model.addAttribute("vehicleList", service.getAllVehicles());
        return "vehicle-list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("vehicle", new Vehicle());
        return "vehicle-form";
    }

    @PostMapping("/add")
    public String addVehicle(@ModelAttribute Vehicle vehicle, Model model) {
        if (service.existsByRegistrationNumber(vehicle.getRegistrationNumber())) {
            model.addAttribute("errorMessage", "Vehicle with this registration number already exists!");
            model.addAttribute("vehicle", vehicle);
            return "vehicle-form"; // Return the form with error
        }
        service.addVehicle(vehicle);
        return "redirect:/vehicles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Vehicle vehicle = service.getVehicleById(id);
        if (vehicle == null) {
            return "redirect:/vehicles"; 
        }
        model.addAttribute("vehicle", vehicle);
        return "vehicle-form";
    }

    @PostMapping("/update")
    public String updateVehicle(@ModelAttribute Vehicle vehicle, Model model) {
        if (service.existsByRegistrationNumber(vehicle.getRegistrationNumber()) &&
                !service.getVehicleById(vehicle.getVehicleId()).getRegistrationNumber().equals(vehicle.getRegistrationNumber())) {

            model.addAttribute("errorMessage", "Cannot update to an existing registration number!");
            model.addAttribute("vehicle", vehicle);
            return "vehicle-form";
        }
        service.updateVehicle(vehicle);
        return "redirect:/vehicles";
    }

    @GetMapping("/delete/{id}")
    public String deleteVehicle(@PathVariable Long id) {
        service.deleteVehicle(id);
        return "redirect:/vehicles";
    }

    @GetMapping("/check-registration")
    @ResponseBody
    public Map<String, Boolean> checkRegistrationNumber(@RequestParam String number) {
        boolean exists = service.existsByRegistrationNumber(number);
        return Map.of("exists", exists);
    }
}
