package com.example.mentalhealthsystem.controller;

import com.example.mentalhealthsystem.Database.Admin;
import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.Database.UserLoginDetails;
import com.example.mentalhealthsystem.repository.AdminRepository;
import com.example.mentalhealthsystem.repository.DoctorRepository;
import com.example.mentalhealthsystem.repository.UserLoginDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/check")
public class CheckEmailController {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private UserLoginDetailsRepository userLoginDetailsRepository;

    @Autowired
    private AdminRepository adminRepository;

    @GetMapping("/check-email")
    public ResponseEntity<?> checkEmailExistencePatient(@RequestParam String email) {
        Optional<UserLoginDetails> user = userLoginDetailsRepository.findById(email);
        return ResponseEntity.ok(user.isPresent());
    }

    @GetMapping("/doctor/check-email")
    public ResponseEntity<?> checkEmailExistenceDoctor(@RequestParam String email) {
        Optional<Doctor> existingUser = doctorRepository.findById(email);
        if (existingUser.isPresent()) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }
    @GetMapping("/admin/check-email")
    public ResponseEntity<?> checkEmailExistenceAdmin(@RequestParam String email) {
        Optional<Admin> existingUser = adminRepository.findById(email);
        if (existingUser.isPresent()) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }
}

