package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.*;
import com.example.mentalhealthsystem.constants.AssessmentInfo;
import com.example.mentalhealthsystem.repository.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class AssessmentService {


    private final AssessmentRepository assessmentRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    private final PatientRepository patientRepository;
    @Autowired
    public AssessmentService(AssessmentRepository assessmentRepository, AnswerRepository answerRepository, QuestionRepository questionRepository, PatientRepository patientRepository) {
        this.assessmentRepository = assessmentRepository;
        this.answerRepository = answerRepository;
        this.questionRepository = questionRepository;
        this.patientRepository = patientRepository;
    }

    public List<String> answerOfQuestion(String assessmentName, HttpServletRequest request) {
        List<String> answers = new ArrayList<>();
        log.error("answerOfQuestion {}", getNumberOfQuestions(assessmentName));
        int numberOfQuestions = getNumberOfQuestions(assessmentName);
        for (int i = 1; i <= numberOfQuestions; i++) {
            String answer = request.getParameter("answer" + i);
            if (answer != null) {
                answers.add(answer);
            }
        }
        return answers;
    }
    public int getNumberOfQuestions(String assessmentName) {
        log.error("getNumberOfQuestions service {}", AssessmentInfo.getNumberOfQuestions(assessmentName));
        return AssessmentInfo.getNumberOfQuestions(assessmentName);
    }
    public int calculateScore(List<String> answers) {
        int totalScore = 0;
        for (String answer : answers) {
            totalScore += Integer.parseInt(answer);
        }
        return totalScore;
    }

    public String getCategory(int totalScore, String symptomName, int numberOfQuestions) {
        log.error("numberOfQuestions * AssessmentInfo.getANSWERS().size()");
        int maxScore = numberOfQuestions * AssessmentInfo.getANSWERS().size();
        double proportion = (double) totalScore / maxScore;
        log.error("translatedSymptomName bafore {}");
        String translatedSymptomName = AssessmentInfo.getAssessmentName(symptomName);
        log.error("translatedSymptomName get {}",translatedSymptomName);
        if (proportion >= 0.0 && proportion < 0.25) {
            return "أعراض " + translatedSymptomName + " ضئيلة";
        } else if (proportion >= 0.25 && proportion < 0.5) {
            return "أعراض " + translatedSymptomName + " خفيفة";
        } else if (proportion >= 0.5 && proportion < 0.75) {
            return "أعراض " + translatedSymptomName + " متوسطة";
        } else if (proportion >= 0.75 && proportion < 1.0) {
            return "أعراض " + translatedSymptomName + " متوسطة إلى شديدة";
        } else if (proportion >= 1.0) {
            return "أعراض " + translatedSymptomName + " شديدة";
        } else {
            throw new IllegalArgumentException("Invalid total score: " + totalScore);
        }
    }



    public void saveAssessmentAndAnswers(String assessmentName, List<String> answers,
                                         @AuthenticationPrincipal UserDetails userDetails, Model model) {
        Optional<Patient> optionalPatient = patientRepository.findById(userDetails.getUsername());
        if (optionalPatient.isPresent()) {
            int totalScore = calculateScore(answers);
            log.error("before getCategory");
            String category = getCategory(totalScore, assessmentName,
                    AssessmentInfo.getNumberOfQuestions(assessmentName));

            model.addAttribute("totalScore", totalScore);
            model.addAttribute("category", category);
            Patient patient = optionalPatient.get();

            Assessment assessment = new Assessment();

            assessment.setName(assessmentName);
            assessment.setUser(patient);
            assessment.setCategory(category);
            assessment.setScore(totalScore);
            assessmentRepository.save(assessment);

            getQuestions(assessmentName, answers, patient, assessment);


        } else {

            throw new IllegalArgumentException("User not found with username: " + userDetails.getUsername());
        }
    }

    private void getQuestions(String assessmentName, List<String> answers, Patient patient, Assessment assessment) {
        Set<String> questionsContent = AssessmentInfo.getAssessmentQuestions(assessmentName);

        AtomicInteger index = new AtomicInteger(0);

        questionsContent.forEach(question -> {
            Question objQuestion = new Question();
            Answer answer = new Answer();
            objQuestion.setContent(question);
            objQuestion.setAssessment(assessment);
            questionRepository.save(objQuestion);
            answer.setAssessment(assessment);
            answer.setContent(answers.get(index.getAndIncrement()));
            answer.setQuestion(objQuestion);
            answer.setQuestion(objQuestion);
            answerRepository.save(answer);
        });
    }


    public void buildAssessment(String assessmentName, Model model)
    {
        model.addAttribute("assessmentQuestions",AssessmentInfo.getAssessmentQuestions(assessmentName) );
        model.addAttribute( "assessmentAnswers", AssessmentInfo.getANSWERS());
        model.addAttribute("assessmentName", assessmentName);
    }


}
