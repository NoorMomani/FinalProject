package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.Admin;
import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.Database.Patient;
import com.example.mentalhealthsystem.Database.UserLoginDetails;
import com.example.mentalhealthsystem.constants.UserRoles;
import com.example.mentalhealthsystem.repository.AdminRepository;
import com.example.mentalhealthsystem.repository.DoctorRepository;
import com.example.mentalhealthsystem.repository.PatientRepository;
import com.example.mentalhealthsystem.repository.UserLoginDetailsRepository;
import com.example.mentalhealthsystem.request.AdminSignUpRequest;
import com.example.mentalhealthsystem.request.DoctorSignUpRequest;
import com.example.mentalhealthsystem.request.PatientSignUpRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

    @Mock
    private UserLoginDetailsRepository userLoginDetailsRepository;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AdminRepository adminRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    SignUpService signUpService;

    @Test
    void testSignUpPatient() {
        PatientSignUpRequest request = new PatientSignUpRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setUserRole(UserRoles.PATIENT);

        UserLoginDetails userLoginDetails = new UserLoginDetails();
        userLoginDetails.setEmail(request.getEmail());
        userLoginDetails.setPassword(passwordEncoder.encode(request.getPassword()));
        userLoginDetails.setRole(request.getUserRole());

        when(userLoginDetailsRepository.save(userLoginDetails)).thenReturn(userLoginDetails);
        when(patientRepository.save(any(Patient.class))).thenReturn(new Patient());
        boolean result = signUpService.signUp(request);

        assertTrue(result);
    }

    @Test
    void testSignUpDoctor() {
        DoctorSignUpRequest request = new DoctorSignUpRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setUserRole(UserRoles.DOCTOR);

        UserLoginDetails userLoginDetails = new UserLoginDetails();
        userLoginDetails.setEmail(request.getEmail());
        userLoginDetails.setPassword(passwordEncoder.encode(request.getPassword()));
        userLoginDetails.setRole(request.getUserRole());

        when(userLoginDetailsRepository.save(userLoginDetails)).thenReturn(userLoginDetails);
        when(doctorRepository.save(any(Doctor.class))).thenReturn(new Doctor());
        boolean result = signUpService.signUp(request);

        assertTrue(result);
    }
    @Test
    void testSignUpAdmin() {
        AdminSignUpRequest request = new AdminSignUpRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setUserRole(UserRoles.ADMIN);

        UserLoginDetails userLoginDetails = new UserLoginDetails();
        userLoginDetails.setEmail(request.getEmail());
        userLoginDetails.setPassword(passwordEncoder.encode(request.getPassword()));
        userLoginDetails.setRole(request.getUserRole());

        when(userLoginDetailsRepository.save(userLoginDetails)).thenReturn(userLoginDetails);
        when(adminRepository.save(any(Admin.class))).thenReturn(new Admin());
        boolean result = signUpService.signUp(request);

        assertTrue(result);
    }

    @Test
    void  testSignUp_ExceptionHandling() {
        AdminSignUpRequest request = new AdminSignUpRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");
        request.setUserRole(UserRoles.ADMIN);

        UserLoginDetails userLoginDetails = new UserLoginDetails();
        userLoginDetails.setEmail(request.getEmail());
        userLoginDetails.setPassword(passwordEncoder.encode(request.getPassword()));
        userLoginDetails.setRole(request.getUserRole());

        when(userLoginDetailsRepository.save(any(UserLoginDetails.class))).thenThrow(new RuntimeException("Failed to save user login details"));

        boolean result = signUpService.signUp(request);

        assertFalse(result);
    }


}