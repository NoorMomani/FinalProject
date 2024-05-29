package com.example.mentalhealthsystem.repository;

import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.constants.DoctorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor,String> {
    Optional<Doctor> findByEmail(String email);
    Optional<List<Doctor>> findAllByStatus(DoctorStatus status);
    Optional<Doctor> findByEmailAndStatus(String email, DoctorStatus status);
}
