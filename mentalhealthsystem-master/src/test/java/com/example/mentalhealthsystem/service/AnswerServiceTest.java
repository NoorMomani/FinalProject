package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.Answer;
import com.example.mentalhealthsystem.repository.AnswerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class AnswerServiceTest {
    @Mock
    AnswerRepository answerRepository;

    @InjectMocks
    AnswerService answerService;

    @Test
    void getAnswersByQuestionIdWhenIdInDBThenReturnAnswerObject() {
        Answer answer = new Answer();
        doReturn(Optional.of(answer)).when(answerRepository).findByQuestionId(2);
        assertEquals(answer, answerService.getAnswersByQuestionId(2));
    }
    @Test
    void getAnswersByQuestionIdWhenIdNotInDBThenReturnNull() {
        assertNull(answerService.getAnswersByQuestionId(2));
    }
}