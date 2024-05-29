package com.example.mentalhealthsystem.controller;

import com.example.mentalhealthsystem.Database.Appointment;
import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.Database.Location;
import com.example.mentalhealthsystem.request.DoctorSignUpRequest;
import com.example.mentalhealthsystem.service.DoctorService;
import com.example.mentalhealthsystem.service.LocationService;
import com.example.mentalhealthsystem.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
@Controller
@RequestMapping("/doctors")
@Slf4j
public class DoctorController {
    @Autowired
    DoctorService doctorService;
    @Autowired
    LocationService locationService;
    @Autowired
    PatientService patientService;

    @GetMapping("/dashboard")
    public String doctorDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {

        String username = userDetails.getUsername();
        if (username != null) {
            Doctor doctor = doctorService.getDoctorById(username);
            List<Appointment> appointments = doctorService.getAppointmentsByDoctorId(doctor.getEmail());
            List<Appointment> bookedAppointments = patientService.getAppointmentsByDoctorEmailAndBookedIsTrue(username);
            model.addAttribute("bookedAppointments", bookedAppointments);
            model.addAttribute("doctor", doctor);
            model.addAttribute("appointments", appointments);
            return "doctor_dashboard";
        } else {
            return "loginPage";
        }
    }

    @PostMapping("/page/{email}")
    public String getDoctorProfile(@PathVariable String email, Model model) {
        Doctor doctor = doctorService.getDoctorById(email);
        model.addAttribute("doctor", doctor);
        List<Appointment> appointments = doctorService.getAppointmentsByDoctorId(email);
        model.addAttribute("appointments", appointments);
        return "doctor_dashboard";
    }

    @PostMapping("/{email}/update")
    public String updateDoctorProfile(@PathVariable String email, @ModelAttribute DoctorSignUpRequest doctorSUR, Model model) {
        try {
            log.error("before updateDoctorProfile");
            doctorService.updateDoctorProfile(email, doctorSUR);
            log.error("after updateDoctorProfile");
            Doctor doctor = doctorService.getDoctorById(email);
            log.error("after  doctorService.getDoctorById(email);");
            model.addAttribute("doctor", doctor);

            return "doctor_dashboard";
        } catch (Exception e) {
            e.printStackTrace();
            return "loginpage";
        }
    }

    @GetMapping("/{email}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String email) {
        Doctor doctor = doctorService.getDoctorById(email);

        if (doctor == null || doctor.getProfilePictureContent() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(doctor.getProfilePictureContent(), headers, HttpStatus.OK);
    }





    @PostMapping("/{email}/appointments/add")
    public String addAppointment(@PathVariable String email,
                                 @RequestParam("appointmentDateTime") LocalDateTime appointmentDateTime, Model model) {

        doctorService.addAppointment(email, appointmentDateTime);
        Doctor doctor = doctorService.getDoctorById(email);
        List<Appointment> appointments = doctorService.getAppointmentsByDoctorId(doctor.getEmail());
        List<Appointment> bookedAppointments = patientService.getAppointmentsByDoctorEmailAndBookedIsTrue(email);
        model.addAttribute("bookedAppointments", bookedAppointments);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);
        return "addAppointments";
    }
    @PostMapping("/{email}/appointments/{appointmentId}/update")
    public String updateAppointment(@PathVariable String email,
                                    @PathVariable Long appointmentId,
                                    @RequestParam("session") LocalDateTime session,Model model) {

        doctorService.updateAppointment(email, appointmentId, session);
        Doctor doctor = doctorService.getDoctorById(email);
        List<Appointment> appointments = doctorService.getAppointmentsByDoctorId(doctor.getEmail());
        List<Appointment> bookedAppointments = patientService.getAppointmentsByDoctorEmailAndBookedIsTrue(email);
        model.addAttribute("bookedAppointments", bookedAppointments);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);
        return "addAppointments";
    }

    @PostMapping("/{email}/appointments/{appointmentId}/delete")
    public String deleteAppointment(@PathVariable String email,
                                    @PathVariable Long appointmentId, Model model) {

        doctorService.deleteAppointment(email, appointmentId);
        Doctor doctor = doctorService.getDoctorById(email);
        List<Appointment> appointments = doctorService.getAppointmentsByDoctorId(doctor.getEmail());
        List<Appointment> bookedAppointments = patientService.getAppointmentsByDoctorEmailAndBookedIsTrue(email);
        model.addAttribute("bookedAppointments", bookedAppointments);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);
        return "addAppointments";
    }

    @GetMapping("/{email}/cv")
    public ResponseEntity<InputStreamResource> getCV(@PathVariable String email) {
        Doctor doctor = doctorService.findByEmail(email);
        if (doctor == null || doctor.getCvContent() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] cvContent = doctor.getCvContent();
        String cvFileName = doctor.getCvFileName();
        String cvContentType = doctor.getCvContentType();

        ByteArrayInputStream bis = new ByteArrayInputStream(cvContent);
        InputStreamResource resource = new InputStreamResource(bis);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + cvFileName + "\"")
                .contentType(MediaType.parseMediaType(cvContentType))
                .body(resource);
    }

    @GetMapping("/{email}/identityLicense")
    public ResponseEntity<InputStreamResource> getIdentityLicense(@PathVariable String email) throws SQLException {
        Doctor doctor = doctorService.findByEmail(email);
        byte[] identityLicenseContent = doctor.getIdentityLicenseContent();
        String identityLicenseFileName = doctor.getIdentityLicenseFileName();
        String identityLicenseContentType = doctor.getIdentityLicenseContentType();

        ByteArrayInputStream bis = new ByteArrayInputStream(identityLicenseContent);
        InputStreamResource resource = new InputStreamResource(bis);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + identityLicenseFileName + "\"")
                .contentType(MediaType.parseMediaType(identityLicenseContentType))
                .body(resource);
    }


    @PostMapping("/{appointmentId}/sendConfirmation")
    public String sendConfirmationEmail(@PathVariable Long appointmentId,@AuthenticationPrincipal UserDetails userDetails,Model model) {
        log.error("controller sendConfirmationEmail enter");
        patientService.sendConfirmationEmail(appointmentId);
        log.error("controller sendConfirmationEmail finish");
        String userEmail = userDetails.getUsername();

        List<Appointment> bookedAppointments = patientService.getAppointmentsByDoctorEmailAndBookedIsTrue(userEmail);
        model.addAttribute("bookedAppointments", bookedAppointments);

        return "availableAppointments";
    }
    @PostMapping("/{appointmentId}/addReport")
    public String addReport(@PathVariable Long appointmentId, @RequestParam("report") String report,@AuthenticationPrincipal UserDetails userDetails,Model model) {
        patientService.addReport(appointmentId, report);
        String userEmail = userDetails.getUsername();
        Doctor doctor = doctorService.findByEmail(userEmail);
        List<Appointment> appointments = doctorService.getAppointmentsByDoctorId(doctor.getEmail());
        List<Appointment> bookedAppointments = patientService.getAppointmentsByDoctorEmailAndBookedIsTrue(userEmail);
        model.addAttribute("bookedAppointments", bookedAppointments);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);
        return "availableAppointments";
    }
    @GetMapping("/availableAppointments")
    public String getavailableAppointments(@AuthenticationPrincipal UserDetails userDetails,Model model) {
        String username = userDetails.getUsername();
        if (username != null) {

            List<Appointment> bookedAppointments = patientService.getAppointmentsByDoctorEmailAndBookedIsTrue(username);
            model.addAttribute("bookedAppointments", bookedAppointments);
            return "availableAppointments";
        } else {
            return "loginPage";
        }
    }

    @GetMapping("/GetAddAppointments")
    public String GetAddAppointments(@AuthenticationPrincipal UserDetails userDetails,Model model) {
        log.error("GetAddAppointments  rerer");
        String username = userDetails.getUsername();
        log.error("doctor username {}",username);
        if (username != null) {
            model.addAttribute("email", username);
            List<Appointment> appointments = doctorService.getAppointmentsByDoctorId(username);
            log.error("get bookedAppointments from service");
            model.addAttribute("appointments", appointments);
            return "addAppointments";
        } else {
            return "loginPage";
        }
    }

    @GetMapping("/EditProfile")
    public String EditProfile(@AuthenticationPrincipal UserDetails userDetails,Model model) {
        log.error("enter EditProfile");
        String username = userDetails.getUsername();
        log.error("username EditProfile {}", username);
        if (username != null) {
            Doctor doctor = doctorService.getDoctorById(username);
            model.addAttribute("doctor", doctor);
            Location location = locationService.getLocationByDoctor(doctor);
            model.addAttribute("location", location);
            log.error("location.getLatitude() {}", location.getLatitude());
            log.error("location.getLongitude() {}", location.getLongitude());
            log.error("after EditProfile");
            return "EditDoctorProfile";
        } else {
            return "loginPage";
        }
    }

}
