package com.example.mentalhealthsystem.repository;

import com.example.mentalhealthsystem.Database.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
}