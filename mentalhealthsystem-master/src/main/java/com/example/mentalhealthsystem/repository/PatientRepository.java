package com.example.mentalhealthsystem.repository;

import com.example.mentalhealthsystem.Database.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

}
