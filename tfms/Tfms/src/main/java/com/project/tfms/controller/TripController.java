package com.project.tfms.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.stereotype.Controller; 
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import com.project.tfms.model.Trip;
import com.project.tfms.model.Vehicle;
import com.project.tfms.service.TripService;
import com.project.tfms.service.VehicleService;




@Controller
@RequestMapping("/trips") 
public class TripController {

@Autowired
private TripService tripService;

@Autowired
private VehicleService vehicleService;

@GetMapping
public String viewTrips(Model model) {
    model.addAttribute("trips", tripService.getAllTrips());
    return "trip-list";
}

@GetMapping("/add")
public String addTripForm(Model model) {
    Trip trip = new Trip();

    model.addAttribute("trip", trip);
    List<Vehicle> a=vehicleService.getAllVehicles();
    model.addAttribute("availableVehicles",a);
    List<String> indianCities = Arrays.asList(
        "Mumbai", "Delhi", "Bengaluru", "Kolkata", "Chennai",
        "Hyderabad", "Ahmedabad", "Pune", "Surat", "Jaipur"
    );
    model.addAttribute("cities", indianCities);
    return "trip-form";
}


@PostMapping("/save")
public String saveTrip(@ModelAttribute Trip trip, Model model) {
    try {
        tripService.saveTrip(trip);
        return "redirect:/trips";
    } catch (Exception e) {
        model.addAttribute("error", e.getMessage());
        model.addAttribute("trip", trip);
        List<Vehicle> a=vehicleService.getAllVehicles();
        model.addAttribute("availableVehicles",a);
        List<String> indianCities = Arrays.asList(
        		"Ahmedabad", "Bengaluru", "Chennai", "Delhi", "Hyderabad",
        		"Mumbai", "Jaipur", "Kolkata", "Pune", "Surat"
        );
        model.addAttribute("cities", indianCities);
        return "trip-form";
    }
}

@GetMapping("/update/{id}")
public String updateTripForm(@PathVariable Long id, Model model) {
    Trip trip = tripService.getTripById(id);
    model.addAttribute("trip", trip);
    List<Vehicle> a=vehicleService.getAllVehicles();
    model.addAttribute("availableVehicles",a);
	List<String> indianCities = Arrays.asList(
			"Ahmedabad", "Bengaluru", "Chennai", "Delhi", "Hyderabad",
			"Mumbai", "Jaipur", "Kolkata", "Pune", "Surat"

        );
        model.addAttribute("cities", indianCities);
        return "trip-form";
}

@GetMapping("/delete/{id}")
public String deleteTrip(@PathVariable Long id) {
    tripService.deleteTrip(id);
    return "redirect:/trips";
}

}



