package com.example.mentalhealthsystem.repository;

import com.example.mentalhealthsystem.Database.Appointment;
import com.example.mentalhealthsystem.Database.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
    List<Report> findByAppointmentIn(List<Appointment> appointments);

}
