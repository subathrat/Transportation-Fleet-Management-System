package com.project.tfms.service;

import com.project.tfms.model.Trip;
import com.project.tfms.repository.TripRepository;
import com.project.tfms.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private TripService tripService;

    private Trip trip1;
    private Trip trip2;

    @BeforeEach
    void setUp() {
        trip1 = new Trip();
        trip1.setTripId(1L);
        trip1.setDriverId(10L);
        trip1.setVehicleId(100L);
        trip1.setStartTime(LocalDateTime.of(2025, 6, 5, 9, 0));
        trip1.setEndTime(LocalDateTime.of(2025, 6, 5, 17, 0));
        trip1.setStartLocation("A");
        trip1.setEndLocation("B");

        trip2 = new Trip();
        trip2.setTripId(2L);
        trip2.setDriverId(11L);
        trip2.setVehicleId(101L);
        trip2.setStartTime(LocalDateTime.of(2025, 6, 6, 8, 0));
        trip2.setEndTime(LocalDateTime.of(2025, 6, 6, 16, 0));
        trip2.setStartLocation("C");
        trip2.setEndLocation("D");
    }

    @Test
    void testGetAllTrips() {
        when(tripRepository.findAll()).thenReturn(Arrays.asList(trip1, trip2));

        List<Trip> result = tripService.getAllTrips();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(trip1.getTripId(), result.get(0).getTripId());
        verify(tripRepository, times(1)).findAll();
    }

    @Test
    void testGetTripByIdFound() {
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip1));

        Trip result = tripService.getTripById(1L);

        assertNotNull(result);
        assertEquals(trip1.getTripId(), result.getTripId());
        verify(tripRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTripByIdNotFound() {
        when(tripRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            tripService.getTripById(99L);
        });

        assertEquals("Trip not found", thrown.getMessage());
        verify(tripRepository, times(1)).findById(99L);
    }

    @Test
    void testSaveNewTripSuccess() {
        Trip newTrip = new Trip();
        newTrip.setDriverId(12L);
        newTrip.setVehicleId(102L);
        newTrip.setStartTime(LocalDateTime.of(2025, 6, 7, 9, 0));
        newTrip.setEndTime(LocalDateTime.of(2025, 6, 7, 17, 0));
        newTrip.setStartLocation("E");
        newTrip.setEndLocation("F");

        when(vehicleRepository.existsById(newTrip.getVehicleId())).thenReturn(true);
        when(tripRepository.findTripsByDriverIdOrVehicleIdAndTimeRange(anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), eq(null)))
                .thenReturn(Collections.emptyList());
        when(tripRepository.save(newTrip)).thenReturn(newTrip);

        tripService.saveTrip(newTrip);

        verify(vehicleRepository, times(1)).existsById(newTrip.getVehicleId());
        verify(tripRepository, times(1)).findTripsByDriverIdOrVehicleIdAndTimeRange(anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), eq(null));
        verify(tripRepository, times(1)).save(newTrip);
    }

    @Test
    void testSaveNewTripThrowsExceptionWhenDriverOrVehicleAssigned() {
        Trip newTrip = new Trip();
        newTrip.setDriverId(10L);
        newTrip.setVehicleId(100L);
        newTrip.setStartTime(LocalDateTime.of(2025, 6, 5, 10, 0)); // Overlaps with trip1
        newTrip.setEndTime(LocalDateTime.of(2025, 6, 5, 18, 0));
        newTrip.setStartLocation("A");
        newTrip.setEndLocation("C");

        when(vehicleRepository.existsById(newTrip.getVehicleId())).thenReturn(true);
        when(tripRepository.findTripsByDriverIdOrVehicleIdAndTimeRange(anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), eq(null)))
                .thenReturn(Arrays.asList(trip1)); // Simulate conflict

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            tripService.saveTrip(newTrip);
        });

        assertEquals("Driver or vehicle is already assigned to another trip during this time.", thrown.getMessage());
        verify(vehicleRepository, times(1)).existsById(newTrip.getVehicleId());
        verify(tripRepository, times(1)).findTripsByDriverIdOrVehicleIdAndTimeRange(anyLong(), anyLong(), any(LocalDateTime.class), any(LocalDateTime.class), eq(null));
        verify(tripRepository, never()).save(any(Trip.class));
    }

    @Test
    void testDeleteTrip() {
        Long idToDelete = 1L;
        doNothing().when(tripRepository).deleteById(idToDelete);

        tripService.deleteTrip(idToDelete);

        verify(tripRepository, times(1)).deleteById(idToDelete);
    }
}