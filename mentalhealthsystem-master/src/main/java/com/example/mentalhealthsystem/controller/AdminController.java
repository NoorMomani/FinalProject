package com.example.mentalhealthsystem.controller;

import com.example.mentalhealthsystem.Database.Admin;
import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.request.AdminSignUpRequest;
import com.example.mentalhealthsystem.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
@Slf4j
@Controller
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    AdminService adminService;
    @GetMapping("/dashboard")
    public String patientDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        String username = userDetails.getUsername();

        if (username != null) {
            Admin admin = adminService.getAdminById(username);
            model.addAttribute("admin", admin);
            return "admin_dashboard";
        } else {
            return "loginPage";
        }
    }

    @GetMapping("/AdminDash")
    public String AdminDash(Model model) {
        model.addAttribute("requestedDoctors", adminService.getRequestedDoctors());
        model.addAttribute("systemDoctors", adminService.getSystemDoctors());
        return "doctors";
    }

    @GetMapping("/adminList")
    public String adminList(Model model) {
        Optional<List<Admin>> adminOptional = Optional.ofNullable(adminService.getRequestedAdmins());
        if (adminOptional.isPresent()) {
            model.addAttribute("requestedAdmins", adminOptional.get());
        } else {
            model.addAttribute("requestedAdmins", List.of());
        }
        Optional<List<Admin>> adminOptional2 = Optional.ofNullable(adminService.getSystemAdmins());
        if (adminOptional2.isPresent()) {
            model.addAttribute("systemAdmins", adminOptional2.get());
        } else {
            model.addAttribute("systemAdmins", List.of());
        }
        return "admins";
    }


    @GetMapping("/doctorDetails/{email}")
    public String getDoctorDetails(@PathVariable String email, Model model) {
        Doctor doctor = adminService.getDoctorByEmail(email);
        model.addAttribute("doctor", doctor);
        return "DoctorDetails";
    }

    @GetMapping("/adminDetails/{email}")
    public String getAdminDetails(@PathVariable String email, Model model) {
        log.error("Entering getAdminDetails method");
        Admin admin = adminService.getAdminByEmail(email);
        log.error("Admin fetched: {}", admin);
        if (admin != null) {
            model.addAttribute("admin", admin);
            return "admin_details";
        } else {
            log.error("Admin not found for email: {}", email);
            return "loginpage";
        }
    }


    @GetMapping("/acceptAdmin/{email}")
    public String acceptdmin(@PathVariable String email, Model model) {
        adminService.acceptAdmins(email);
        Optional<List<Admin>> adminOptional = Optional.ofNullable(adminService.getRequestedAdmins());
        if (adminOptional.isPresent()) {
            model.addAttribute("requestedAdmins", adminOptional.get());
        } else {
            model.addAttribute("requestedAdmins", List.of());
        }
        Optional<List<Admin>> adminOptional2 = Optional.ofNullable(adminService.getSystemAdmins());
        if (adminOptional2.isPresent()) {
            model.addAttribute("systemAdmins", adminOptional2.get());
        } else {
            model.addAttribute("systemAdmins", List.of());
        }
        return "admins";
    }

    @GetMapping("/acceptDoctor/{email}")
    public String acceptDoctor(@PathVariable String email, Model model) {
        Doctor doctor = adminService.acceptDoctors(email);
        model.addAttribute("requestedDoctors", adminService.getRequestedDoctors());
        model.addAttribute("systemDoctors", adminService.getSystemDoctors());
        return "doctors";
    }

    @GetMapping("/rejectDoctor/{email}")
    public String rejectDoctor(@PathVariable String email, Model model) {
        Doctor doctor = adminService.rejectDoctors(email);
        model.addAttribute("requestedDoctors", adminService.getRequestedDoctors());
        model.addAttribute("systemDoctors", adminService.getSystemDoctors());
        return "doctors";
    }

    @GetMapping("/rejectAdmin/{email}")
    public String rejectAdmin(@PathVariable String email, Model model) {
        adminService.rejectAdmins(email);
        Optional<List<Admin>> adminOptional = Optional.ofNullable(adminService.getRequestedAdmins());
        if (adminOptional.isPresent()) {
            model.addAttribute("requestedAdmins", adminOptional.get());
        } else {
            model.addAttribute("requestedAdmins", List.of());
        }
        Optional<List<Admin>> adminOptional2 = Optional.ofNullable(adminService.getSystemAdmins());
        if (adminOptional2.isPresent()) {
            model.addAttribute("systemAdmins", adminOptional2.get());
        } else {
            model.addAttribute("systemAdmins", List.of());
        }
        return "admins";
    }
    @PostMapping("/deleteDoctor")
    public String deleteDoctor(@RequestParam("email") String email, Model model) {
        Doctor doctor = adminService.getDoctorByEmail(email);
        if (doctor != null) {
            adminService.sendDeleteEmail(doctor);
            adminService.deleteDoctor(email);

            model.addAttribute("message", "Application rejected successfully.");
            model.addAttribute("requestedDoctors", adminService.getRequestedDoctors());
            model.addAttribute("systemDoctors", adminService.getSystemDoctors());
        } else {
            model.addAttribute("message", "Error rejecting application.");
        }
        return "doctors";
    }


    @PostMapping("/deleteAdmin")
    public String deleteAdmin(@RequestParam("email") String email, Model model) {
        Admin admin = adminService.getAdminByEmail(email);
        if (admin != null) {

            adminService.sendDeleteEmail(admin);
            adminService.deleteAdmin(email);
            Optional<List<Admin>> adminOptional = Optional.ofNullable(adminService.getRequestedAdmins());
            if (adminOptional.isPresent()) {
                model.addAttribute("requestedAdmins", adminOptional.get());
            } else {
                model.addAttribute("requestedAdmins", List.of());
            }
            Optional<List<Admin>> adminOptional2 = Optional.ofNullable(adminService.getSystemAdmins());
            if (adminOptional2.isPresent()) {
                model.addAttribute("systemAdmins", adminOptional2.get());
            } else {
                model.addAttribute("systemAdmins", List.of());
            }
        } else {
            model.addAttribute("message", "Error rejecting application.");
        }
        return "admins";
    }


    @PostMapping("/searchRequestedDoctors")
    public String searchSubmittedDoctorsByEmail(@RequestParam String email, Model model) {
        log.error("searchSubmittedDoctorsByEmail before");
        Optional<Doctor> doctorOptional = adminService.findPendingDoctorByEmail(email);
        log.error("searchSubmittedDoctorsByEmail after");
        model.addAttribute("systemDoctors", adminService.getSystemDoctors());
        if (doctorOptional.isPresent()) {
            model.addAttribute("requestedDoctors", List.of(doctorOptional.get()));
        } else {
            model.addAttribute("requestedDoctors", List.of());
        }
        return "doctors";
    }

    @PostMapping("/searchRequestedAdmins")
    public String searchRequestedAdminsByEmail(@RequestParam String email, Model model) {
        Optional<Admin> adminOptional = adminService.findPendingAdminByEmail(email);
        model.addAttribute("systemAdmins", adminService.getSystemAdmins());
        if (adminOptional.isPresent()) {
            model.addAttribute("requestedAdmins", List.of(adminOptional.get()));
        } else {
            model.addAttribute("requestedAdmins", List.of());
        }
        return "admins";
    }

    @GetMapping("/searchSystemDoctors")
    public String searchSystemDoctorsByEmail(@RequestParam String email, Model model) {
        Optional<Doctor> doctorOptional = adminService.findApprovedDoctorByEmail(email);
        model.addAttribute("requestedDoctors", adminService.getRequestedDoctors());
        if (doctorOptional.isPresent()) {
            model.addAttribute("systemDoctors", List.of(doctorOptional.get()));
        } else {
            model.addAttribute("systemDoctors", List.of());
        }
        return "doctors";
    }


    @GetMapping("/searchSystemAdmins")
    public String searchSystemAdminsByEmail(@RequestParam String email, Model model) {
        Optional<Admin> adminOptional = adminService.findApprovedAdminByEmail(email);
        model.addAttribute("requestedAdmins", adminService.getRequestedAdmins());
        if (adminOptional.isPresent()) {
            model.addAttribute("systemAdmins", List.of(adminOptional.get()));
        } else {
            model.addAttribute("systemAdmins", List.of());
        }
        return "admins";
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestParam("email") String email,
                                       @RequestParam("newPassword") String newPassword,
                                       RedirectAttributes redirectAttributes, Model model) {
        adminService.changePassword(email, newPassword);
        redirectAttributes.addFlashAttribute("message", "Password changed successfully.");
        Doctor doctor = adminService.getDoctorByEmail(email);
        model.addAttribute("doctor", doctor);
        return "DoctorDetails";
    }
    @GetMapping("/EditProfile")
    public String EditProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        Admin admin = adminService.getAdminById(userDetails.getUsername());
        log.error("userDetails.getUsername() admin {}", userDetails.getUsername());
        model.addAttribute("admin", admin);
        return "EditAdminProfile";
    }


    @PostMapping("/{email}/update")
    public String updatePatientProfile(@PathVariable String email, @ModelAttribute AdminSignUpRequest adminSUR, Model model) {

        try {
            adminService.updateAdmin(email, adminSUR);
            Admin admin = adminService.getAdminById(email);
            model.addAttribute("admin", admin);
            return "admin_dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            return "login";
        }
    }

}
