package com.example.mentalhealthsystem.controller;

import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.constants.UserRoles;
import com.example.mentalhealthsystem.request.AdminSignUpRequest;
import com.example.mentalhealthsystem.request.DoctorSignUpRequest;
import com.example.mentalhealthsystem.request.PatientSignUpRequest;
import com.example.mentalhealthsystem.service.DoctorService;
import com.example.mentalhealthsystem.service.LocationService;
import com.example.mentalhealthsystem.service.SignUpService;
import com.example.mentalhealthsystem.service.ValidatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("signup")
@Slf4j
public class SignUpController {

    @Autowired
    SignUpService signUpService;
    @Autowired
    DoctorService doctorService;
    @Autowired
    LocationService locationService;

    @Autowired
    ValidatorService validatorService;

    @PostMapping("/patient_signup")
    public String signUpPatient(@ModelAttribute("patientSignUpRequest") PatientSignUpRequest request, Model model) {
        log.error("patient_signup enter");
        if (validatorService.isUsedEmail(request.getEmail())){
            model.addAttribute("signupRedirect", "/signup/Join/Us/As/Patient");
            return "failed_signup";
        }

        request.setUserRole(UserRoles.PATIENT);
        if (!signUpService.signUp(request)) {
            return "signup_failed";
        }
        return "loginPage";
    }

    @PostMapping("/doctor_signup")
    public String signUpDoctor(@ModelAttribute("doctorSignUpRequest") DoctorSignUpRequest request, Model model) {
       log.error("signUpDoctor enter");
        if (validatorService.isUsedEmail(request.getEmail())){
            model.addAttribute("signupRedirect", "/signup/Join/Us/As/Specialist");
            return "failed_signup";
        }
        request.setUserRole(UserRoles.DOCTOR);

        try {
            MultipartFile cv = request.getCv();
            if (cv != null && !cv.isEmpty()) {
                log.info("CV file name: {}", cv.getOriginalFilename());
                log.info("CV content type: {}", cv.getContentType());
                request.setCvFileName(cv.getOriginalFilename());
                request.setCvContent(cv.getBytes());
                request.setCvContentType(cv.getContentType());
            }

            MultipartFile identityLicense = request.getIdentityLicense();
            if (identityLicense != null && !identityLicense.isEmpty()) {
                log.info("Identity License file name: {}", identityLicense.getOriginalFilename());
                log.info("Identity License content type: {}", identityLicense.getContentType());
                request.setIdentityLicenseFileName(identityLicense.getOriginalFilename());
                request.setIdentityLicenseContent(identityLicense.getBytes());
                request.setIdentityLicenseContentType(identityLicense.getContentType());
            }


        } catch (IOException e) {
            log.error("Error uploading files", e);
            return "loginpage";
        }

        if (!signUpService.signUp(request)) {
            return "signup_failed";
        }
        String location = request.getLocation();
        String username = request.getEmail();
        log.error("username {}", username);
        String[] latLng = location.split(", ");
        double latitude = Double.parseDouble(latLng[0]);
        double longitude = Double.parseDouble(latLng[1]);
        Doctor doctor = null;
        if (username != null) {
            doctor = doctorService.getDoctorById(username);
        }
        locationService.saveLocation(latitude, longitude , doctor);

        return "loginPage";
    }

    @GetMapping("/admins")
    public String adminsSignUp(Model model) {
        model.addAttribute("adminSignUpRequest", new AdminSignUpRequest());
        return "signup_admin";
    }

    @PostMapping("/admin_signup")
    public String signUpAdmin(@ModelAttribute("adminSignUpRequest") AdminSignUpRequest request) {
        log.info("Received admin signup request: {}", request);
        request.setUserRole(UserRoles.ADMIN);

        if (!signUpService.signUp(request)) {
            return "signup_failed";
        }
        return "loginPage";
    }
    @GetMapping("/Join/Us/As/Specialist")
    public String getJoinUsAsSpecialist(Model model) {
        model.addAttribute("doctorSignUpRequest", new DoctorSignUpRequest());
        log.error("before JoinUsAsSpecialist");

        return "JoinUsAsSpecialist";
    }

    @GetMapping("/Join/Us/As/Patient")
    public String getJoinUsAsPatient(Model model) {
        model.addAttribute("patientSignUpRequest", new PatientSignUpRequest());
        return "signup_patient";
    }
}
