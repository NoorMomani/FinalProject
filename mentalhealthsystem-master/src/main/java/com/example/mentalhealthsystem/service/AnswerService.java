package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.Answer;
import com.example.mentalhealthsystem.repository.AnswerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AnswerService {

    @Autowired
    AnswerRepository answerRepository;
    public Answer getAnswersByQuestionId(int questionId) {
        return answerRepository.findByQuestionId(questionId).orElse(null);

    }
}