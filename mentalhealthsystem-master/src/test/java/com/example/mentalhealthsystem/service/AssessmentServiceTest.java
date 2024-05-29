package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.Answer;
import com.example.mentalhealthsystem.Database.Assessment;
import com.example.mentalhealthsystem.Database.Patient;
import com.example.mentalhealthsystem.Database.Question;
import com.example.mentalhealthsystem.constants.AssessmentInfo;
import com.example.mentalhealthsystem.constants.UserRoles;
import com.example.mentalhealthsystem.repository.AnswerRepository;
import com.example.mentalhealthsystem.repository.AssessmentRepository;
import com.example.mentalhealthsystem.repository.PatientRepository;
import com.example.mentalhealthsystem.repository.QuestionRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AssessmentServiceTest {
    @Mock
    Model model;
    @Mock
    private HttpServletRequest request;
    @Mock
    AnswerRepository answerRepository;
    @Mock
    PatientRepository patientRepository;

    @Mock
    QuestionRepository questionRepository;
    @Mock
    AssessmentRepository assessmentRepository;
    @InjectMocks
    AssessmentService assessmentService;
    @Test
    void answerOfQuestion() {
        Assessment assessment = new Assessment();
        assessment.setName("anxiety");

        when(request.getParameter("answer1")).thenReturn("Answer 1");
        when(request.getParameter("answer2")).thenReturn("Answer 2");

        List<String> answers = assessmentService.answerOfQuestion(assessment.getName(),request);

        assertNotNull(answers);

    }

    @Test
    void getNumberOfQuestions() {
        Assessment assessment = new Assessment();
        assessment.setName("anxiety");
        int number = 7;
        int numberOfQuestions = assessmentService.getNumberOfQuestions(assessment.getName());
        assertEquals(number,numberOfQuestions);
    }

    @Test
    void calculateScore() {
        List<String> answers = Arrays.asList("0", "1", "2");;
        assertEquals(3,assessmentService.calculateScore(answers));
    }

    @Test
    void getCategoryWhenSymptomsAreMinimal() {
     Assessment assessment = new Assessment();
     assessment.setName("anxiety");
     String assessmentName = AssessmentInfo.getAssessmentName(assessment.getName());
     assertEquals("أعراض "  +assessmentName+ " ضئيلة",assessmentService.getCategory(5, assessment.getName(),7 ));
    }
    @Test
    void getCategoryWhenSymptomsAreMild() {
        Assessment assessment = new Assessment();
        assessment.setName("anxiety");
        String assessmentName = AssessmentInfo.getAssessmentName(assessment.getName());
        assertEquals("أعراض "  +assessmentName+ " خفيفة",assessmentService.getCategory(11, assessment.getName(),7 ));
    }
    @Test
    void getCategoryWhenSymptomsAreModerate() {
        Assessment assessment = new Assessment();
        assessment.setName("anxiety");
        String assessmentName = AssessmentInfo.getAssessmentName(assessment.getName());
        assertEquals("أعراض "  +assessmentName+ " متوسطة",assessmentService.getCategory(24, assessment.getName(),7 ));
    }
    @Test
    void getCategoryWhenSymptomsAreModerateToSevere() {
        Assessment assessment = new Assessment();
        assessment.setName("anxiety");
        String assessmentName = AssessmentInfo.getAssessmentName(assessment.getName());
        assertEquals("أعراض "  +assessmentName+ " متوسطة إلى شديدة",assessmentService.getCategory(30, assessment.getName(),7 ));
    }
    @Test
    void getCategoryWhenSymptomsAreSevere() {
        Assessment assessment = new Assessment();
        assessment.setName("anxiety");
        String assessmentName = AssessmentInfo.getAssessmentName(assessment.getName());
        assertEquals("أعراض "  +assessmentName+ " شديدة",assessmentService.getCategory(45, assessment.getName(),7 ));
    }
    @Test
    void getCategoryWhenInvalidTotalScore() {
        Assessment assessment = new Assessment();
        assessment.setName("anxiety");
        assertThrows(IllegalArgumentException.class, () ->assessmentService.getCategory(-10, assessment.getName(),7 ));
    }

    @Test
    void saveAssessmentAndAnswers() {
        Patient patient = new Patient();
        when(patientRepository.findById("test")).thenReturn(Optional.of(patient));
        Assessment assessment = new Assessment();
        assessment.setName("anxiety");
        UserDetails userDetails = User.builder()
                .username("test")
                .password("12345")
                .roles(UserRoles.PATIENT.name())
                .build();
        when(answerRepository.save(any(Answer.class))).thenReturn(new Answer());
        when(questionRepository.save(any(Question.class))).thenReturn(new Question());
        when(assessmentRepository.save(any(Assessment.class))).thenReturn(new Assessment());
        assessmentService.saveAssessmentAndAnswers(assessment.getName(), Arrays.asList("1", "2","3","2","3","1","2"), userDetails, model);
        verify(answerRepository,times(7)).save(any(Answer.class));


    }

    @Test
    void buildAssessment() {
        Assessment assessment = new Assessment();
        assessment.setName("anxiety");

        model.addAttribute("assessmentQuestions",AssessmentInfo.getAssessmentQuestions(assessment.getName()) );
        model.addAttribute( "assessmentAnswers", AssessmentInfo.getANSWERS());
        model.addAttribute("assessmentName", assessment.getName());

        verify(model, times(3)).addAttribute(anyString(),any());

    }
}