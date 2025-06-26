package com.project.tfms.service;

import com.project.tfms.model.Fuel;
import com.project.tfms.repository.FuelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FuelServiceTest {

    @Mock
    private FuelRepository fuelRepository;

    @InjectMocks
    private FuelService fuelService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddFuelRecord() {
        Fuel fuel = new Fuel();
        fuel.setFuelId(1L);
        fuel.setVehicleId(101L);
        fuel.setDate(LocalDateTime.now());
        fuel.setFuelQuantity(50.0);
        fuel.setCost(5000.0);

        when(fuelRepository.save(fuel)).thenReturn(fuel);

        fuelService.addFuelRecord(fuel);

        verify(fuelRepository, times(1)).save(fuel);
    }

    @Test
    void testGetAllFuelRecords() {
        Fuel fuel1 = new Fuel();
        fuel1.setFuelId(1L);
        Fuel fuel2 = new Fuel();
        fuel2.setFuelId(2L);

        List<Fuel> fuelList = Arrays.asList(fuel1, fuel2);

        when(fuelRepository.findAll()).thenReturn(fuelList);

        List<Fuel> result = fuelService.getAllFuelRecords();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(fuel1.getFuelId(), result.get(0).getFuelId());
        assertEquals(fuel2.getFuelId(), result.get(1).getFuelId());
        verify(fuelRepository, times(1)).findAll();
    }

    @Test
    void testDeleteFuelRecord() {
        Long fuelIdToDelete = 1L;

        fuelService.deleteFuelRecord(fuelIdToDelete);

        verify(fuelRepository, times(1)).deleteById(fuelIdToDelete);
    }

    @Test
    void testDeleteFuelRecordWhenNotFound() {
        Long nonExistentFuelId = 99L;
        doNothing().when(fuelRepository).deleteById(nonExistentFuelId);

        fuelService.deleteFuelRecord(nonExistentFuelId);

        verify(fuelRepository, times(1)).deleteById(nonExistentFuelId);
    }
}