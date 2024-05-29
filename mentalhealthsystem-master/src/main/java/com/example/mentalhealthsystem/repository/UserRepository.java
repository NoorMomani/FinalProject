package com.example.mentalhealthsystem.repository;

import com.example.mentalhealthsystem.Database.Patient;
import com.example.mentalhealthsystem.Database.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<Patient> findByEmail(String email);
}
