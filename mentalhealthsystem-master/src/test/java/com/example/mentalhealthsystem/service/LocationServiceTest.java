package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.Database.Location;
import com.example.mentalhealthsystem.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {
    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    LocationService locationService;

    @Test
    void getLocationByDoctorWhenDoctorHaveLocationThenReturnLocation() {
        Doctor doctor = new Doctor();
        Location location = new Location();
        doReturn(location).when(locationRepository).findByDoctor(doctor);
        assertEquals(location, locationService.getLocationByDoctor(doctor));
    }
    @Test
    void getLocationByDoctorWhenDoctorDonNotHaveLocationThenReturnException() {
        Doctor doctor = new Doctor();
        when(locationRepository.findByDoctor(doctor)).thenThrow(new RuntimeException("Failed to retrieve location"));
        assertNull(locationService.getLocationByDoctor(doctor));
    }
    @Test
    void updateLocationWhenCorrectLocationThenUpdateLocation() {
        Doctor doctor = new Doctor();
        doctor.setEmail("test@gmail.com");
        double latitude = 40.0;
        double longitude = -74.0060;

        Location location = new Location();
        location.setLatitude(30.0);
        location.setLongitude(20.0);

        doReturn(location).when(locationRepository).findByDoctor_Email(doctor.getEmail());
        when(locationRepository.save(location)).thenReturn(location);

        Location updateLocation =  locationService.updateLocation(latitude, longitude, doctor);

        assertNotNull(updateLocation);

    }

    @Test
    void saveLocation() {
        Doctor doctor = new Doctor();
        doctor.setEmail("test@gmail.com");
        double latitude = 40.0;
        double longitude = -74.0060;


        when(locationRepository.save(any(Location.class))).thenReturn(new Location());

        Location savedLocation = locationService.saveLocation(latitude, longitude, doctor);
        assertNotNull(savedLocation);
    }
}