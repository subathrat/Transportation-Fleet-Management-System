package com.project.tfms.service;

import com.project.tfms.model.Fuel;
import com.project.tfms.repository.FuelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FuelService {

    @Autowired
    private FuelRepository fuelRepository;

    public void addFuelRecord(Fuel fuel) {
        fuelRepository.save(fuel);
    }

    public List<Fuel> getAllFuelRecords() {
        return fuelRepository.findAll();
    }

    public void deleteFuelRecord(Long fuelId) {

        fuelRepository.deleteById(fuelId);
    }
}