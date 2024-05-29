package com.example.mentalhealthsystem.service;


import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.Database.Location;
import com.example.mentalhealthsystem.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {


    private LocationRepository locationRepository;
    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location getLocationByDoctor(Doctor doctor) {
        try {
            return locationRepository.findByDoctor(doctor);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Location updateLocation(double latitude, double longitude, Doctor doctor) {

        Location location = locationRepository.findByDoctor_Email(doctor.getEmail());

        location.setLatitude(latitude);
        location.setLongitude(longitude);
        return locationRepository.save(location);
    }


    public Location saveLocation(double latitude, double longitude, Doctor doctor) {
        Location location = new Location();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setDoctor(doctor);
        return locationRepository.save(location);
    }
}
