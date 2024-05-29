package com.example.mentalhealthsystem.controller;

import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LoginController {
    @Autowired
    DoctorService doctorService;
    @GetMapping(value = "/login")
    public String login1() {
        return "loginPage";
    }
    @GetMapping(value = "/homepage")
    public String homepage(Model model) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "homepage";
    }
    @GetMapping(value = "/")
    public String login() {
        return "loginPage";
    }

    @GetMapping("/login_failed")
    public String authenticationFailed(Model model) {
        return "login_failed";
    }

}
