package com.example.mentalhealthsystem.repository;

import com.example.mentalhealthsystem.Database.Admin;
import com.example.mentalhealthsystem.constants.AdminStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin,String> {
    Optional<Admin> findByEmail(String email);
  Optional<Admin> findByEmailAndStatus(String email, AdminStatus status);

  Optional<List<Admin>> findAllByStatus(AdminStatus adminStatus);
}
