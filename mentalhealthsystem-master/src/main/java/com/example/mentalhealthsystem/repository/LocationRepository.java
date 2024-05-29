package com.example.mentalhealthsystem.repository;


import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.Database.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    Location findByDoctor(Doctor doctor);
    Location findByDoctor_Email(String doctorId);
}

