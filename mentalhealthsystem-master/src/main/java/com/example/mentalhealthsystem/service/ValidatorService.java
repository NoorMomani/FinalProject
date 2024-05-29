package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.constants.AdminStatus;
import com.example.mentalhealthsystem.constants.DoctorStatus;
import com.example.mentalhealthsystem.repository.AdminRepository;
import com.example.mentalhealthsystem.repository.DoctorRepository;
import com.example.mentalhealthsystem.repository.UserLoginDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidatorService {
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserLoginDetailsRepository userLoginDetailsRepository;



    public boolean isActiveDoctor(String email) {
        return doctorRepository.findByEmailAndStatus(email, DoctorStatus.APPROVED).isPresent();
    }

    public boolean isActiveAdmin(String email) {
        return adminRepository.findByEmailAndStatus(email, AdminStatus.APPROVED).isPresent();
    }

    public boolean isUsedEmail(String email) {
        return userLoginDetailsRepository.findByEmail(email).isPresent();
    }
}
