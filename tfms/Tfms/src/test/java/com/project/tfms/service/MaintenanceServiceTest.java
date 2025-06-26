package com.project.tfms.service;

import com.project.tfms.model.Maintenance;
import com.project.tfms.model.Vehicle;
import com.project.tfms.repository.MaintenanceRepository;
import com.project.tfms.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MaintenanceServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @InjectMocks
    private MaintenanceService maintenanceService;

    private Maintenance maintenance1;
    private Maintenance maintenance2;
    private Vehicle vehicle1;

    @BeforeEach
    void setUp() {
        vehicle1 = new Vehicle();
        vehicle1.setVehicleId(1L);
        vehicle1.setRegistrationNumber("KA01AB1234");

        maintenance1 = new Maintenance();
        maintenance1.setMaintenanceId(101L);
        maintenance1.setVehicleId(1L);
        maintenance1.setDescription("Oil Change");
        maintenance1.setScheduledDate(LocalDate.of(2025, 6, 10));
        maintenance1.setStatus("Scheduled");

        maintenance2 = new Maintenance();
        maintenance2.setMaintenanceId(102L);
        maintenance2.setVehicleId(1L);
        maintenance2.setDescription("Tire Rotation");
        maintenance2.setScheduledDate(LocalDate.of(2025, 7, 15));
        maintenance2.setStatus("Scheduled");
    }

    @Test
    void testScheduleMaintenanceSuccess() {
        when(maintenanceRepository.existsByVehicleIdAndDescriptionAndScheduledDate(
                maintenance1.getVehicleId(),
                maintenance1.getDescription(),
                maintenance1.getScheduledDate())).thenReturn(false);
        when(maintenanceRepository.save(maintenance1)).thenReturn(maintenance1);

        Maintenance result = maintenanceService.scheduleMaintenance(maintenance1);

        assertNotNull(result);
        assertEquals(maintenance1.getDescription(), result.getDescription());
        verify(maintenanceRepository, times(1)).existsByVehicleIdAndDescriptionAndScheduledDate(anyLong(), anyString(), any(LocalDate.class));
        verify(maintenanceRepository, times(1)).save(maintenance1);
    }

    @Test
    void testScheduleMaintenanceThrowsExceptionWhenAlreadyExists() {
        when(maintenanceRepository.existsByVehicleIdAndDescriptionAndScheduledDate(
                maintenance1.getVehicleId(),
                maintenance1.getDescription(),
                maintenance1.getScheduledDate())).thenReturn(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.scheduleMaintenance(maintenance1);
        });

        assertEquals("A maintenance task with the same vehicle, description, and scheduled date already exists.", thrown.getMessage());
        verify(maintenanceRepository, times(1)).existsByVehicleIdAndDescriptionAndScheduledDate(anyLong(), anyString(), any(LocalDate.class));
        verify(maintenanceRepository, never()).save(any(Maintenance.class));
    }



    @Test
    void testUpdateMaintenanceThrowsExceptionWhenConflictExists() {
        Maintenance updateAttempt = new Maintenance();
        updateAttempt.setMaintenanceId(101L);
        updateAttempt.setVehicleId(1L);
        updateAttempt.setDescription("Tire Rotation");
        updateAttempt.setScheduledDate(LocalDate.of(2025, 7, 15));
        updateAttempt.setStatus("Scheduled");

        when(maintenanceRepository.findById(101L)).thenReturn(Optional.of(maintenance1));
        when(maintenanceRepository.existsByVehicleIdAndDescriptionAndScheduledDate(
                updateAttempt.getVehicleId(),
                updateAttempt.getDescription(),
                updateAttempt.getScheduledDate())).thenReturn(true);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            maintenanceService.updateMaintenance(updateAttempt);
        });

        assertEquals("Cannot update: Another maintenance task with the same vehicle, description, and scheduled date already exists.", thrown.getMessage());
        verify(maintenanceRepository, times(1)).findById(101L);
        verify(maintenanceRepository, times(1)).existsByVehicleIdAndDescriptionAndScheduledDate(anyLong(), anyString(), any(LocalDate.class));
        verify(maintenanceRepository, never()).save(any(Maintenance.class));
    }

    @Test
    void testDeleteMaintenance() {
        Long idToDelete = 101L;
        doNothing().when(maintenanceRepository).deleteById(idToDelete);

        maintenanceService.deleteMaintenance(idToDelete);

        verify(maintenanceRepository, times(1)).deleteById(idToDelete);
    }
}