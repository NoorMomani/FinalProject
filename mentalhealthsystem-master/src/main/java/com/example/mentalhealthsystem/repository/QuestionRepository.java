package com.example.mentalhealthsystem.repository;


import com.example.mentalhealthsystem.Database.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByAssessmentName(String assessmentName);
    List<Question> findByAssessmentId(Long assessmentId);
}
