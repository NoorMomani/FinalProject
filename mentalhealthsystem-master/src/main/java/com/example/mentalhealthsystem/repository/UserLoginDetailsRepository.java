package com.example.mentalhealthsystem.repository;

import com.example.mentalhealthsystem.Database.UserLoginDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserLoginDetailsRepository extends JpaRepository<UserLoginDetails, String> {
    Optional<UserLoginDetails> findByEmail(String email);
}
