package com.project.tfms.service;

import com.project.tfms.model.Vehicle;
import com.project.tfms.repository.VehicleRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleService vehicleService;

    @Test
    void testGetAllVehicles() {
        Vehicle v1 = new Vehicle();
        Vehicle v2 = new Vehicle();
        when(vehicleRepository.findAll()).thenReturn(Arrays.asList(v1, v2));

        List<Vehicle> result = vehicleService.getAllVehicles();

        assertEquals(2, result.size());
    }

    @Test
    void testIsRegisteredVehicleId() {
        Long vehicleId = 1L;
        when(vehicleRepository.existsById(vehicleId)).thenReturn(true);

        boolean exists = vehicleService.isRegisteredVehicleId(vehicleId);

        assertTrue(exists);
    }

    @Test
    void testGetAllVehicleIds() {
        Vehicle v1 = new Vehicle();
        v1.setVehicleId(1L);
        Vehicle v2 = new Vehicle();
        v2.setVehicleId(2L);
        when(vehicleRepository.findAll()).thenReturn(Arrays.asList(v1, v2));

        List<Long> ids = vehicleService.getAllVehicleIds();

        assertEquals(Arrays.asList(1L, 2L), ids);
    }

    @Test
    void testExistsByRegistrationNumber() {
        String regNum = "TN01AB1234";
        when(vehicleRepository.findByRegistrationNumber(regNum)).thenReturn(Optional.of(new Vehicle()));

        boolean exists = vehicleService.existsByRegistrationNumber(regNum);

        assertTrue(exists);
    }

    @Test
    void testAddVehicle() {
        Vehicle v = new Vehicle();
        vehicleService.addVehicle(v);

        verify(vehicleRepository, times(1)).save(v);
    }

    @Test
    void testGetVehicleById_Found() {
        Long id = 1L;
        Vehicle v = new Vehicle();
        when(vehicleRepository.findById(id)).thenReturn(Optional.of(v));

        Vehicle result = vehicleService.getVehicleById(id);

        assertNotNull(result);
    }

    @Test
    void testGetVehicleById_NotFound() {
        Long id = 2L;
        when(vehicleRepository.findById(id)).thenReturn(Optional.empty());

        Vehicle result = vehicleService.getVehicleById(id);

        assertNull(result);
    }

    @Test
    void testUpdateVehicle() {
        Vehicle v = new Vehicle();
        vehicleService.updateVehicle(v);

        verify(vehicleRepository, times(1)).save(v);
    }

    @Test
    void testDeleteVehicle() {
        Long id = 5L;
        vehicleService.deleteVehicle(id);

        verify(vehicleRepository, times(1)).deleteById(id);
    }
}
