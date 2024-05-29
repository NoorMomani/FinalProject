package com.example.mentalhealthsystem.repository;


import com.example.mentalhealthsystem.Database.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorEmail(String doctorId);
    List<Appointment> findByDoctorEmailAndBooked
            (String doctorId, boolean booked)
            ;
    List<Appointment> findByPatient_Email(String patientId);
}
