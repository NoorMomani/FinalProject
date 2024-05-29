package com.example.mentalhealthsystem.controller;

import com.example.mentalhealthsystem.Database.Appointment;
import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.Database.Patient;
import com.example.mentalhealthsystem.Database.Report;
import com.example.mentalhealthsystem.request.PatientSignUpRequest;
import com.example.mentalhealthsystem.service.DoctorService;
import com.example.mentalhealthsystem.service.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    PatientService patientService;
    @Autowired
    DoctorService doctorService;

    @GetMapping("/assessments")
    public String assessment(){
        return "/assessments";
    }

    @GetMapping("/dashboard")
    public String patientDashboard(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        String email = userDetails.getUsername();

        if (email != null) {
            Patient patient = patientService.getPatientById(email);
            List<Appointment> appointment = patientService.getAppointmentsByPatientEmail(email);
            model.addAttribute("patient", patient);
            model.addAttribute("appointments", appointment);
            List<Report> reports = patientService.getReportsForPatient(email);
            model.addAttribute("reports", reports);
            return "patient_dashboard";
        } else {
            return "loginPage";
        }
    }

    @GetMapping("/page/{email}")
    public String getPatientProfile(@PathVariable String email, Model model) {
        Patient patient = patientService.getPatientById(email);
        model.addAttribute("patient", patient);
        // Add other necessary attributes
        return "patient_dashboard";
    }
    @PostMapping("/{email}/update")
    public String updatePatientProfile(@PathVariable String email, @ModelAttribute PatientSignUpRequest patientSUR,Model model) {

        try {
            patientService.updatePatientProfile(email, patientSUR);
            Patient patient = patientService.getPatientById(email);
            List<Appointment> appointments = patientService.getAppointmentsByPatientEmail(email);
            List<Report> reports = patientService.getReportsForPatient(email);
            model.addAttribute("patient", patient);
            model.addAttribute("appointments", appointments);
            model.addAttribute("reports", reports);
            return "patient_dashboard";
        } catch (Exception e) {
            // Print the stack trace for debugging
            e.printStackTrace();
            // Redirect to an error page or handle the error appropriately
            return "login";
        }
    }
    @GetMapping("/Doctor/List")
    public String getDoctorList(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<Doctor> doctors = doctorService.getAllDoctors();
        model.addAttribute("doctors", doctors);
        return "DoctorList";
    }


    @GetMapping("/{email}")
    public String getDoctorDetails(@PathVariable String email, Model model) {
        Doctor doctor = doctorService.getDoctorById(email);
        List<Appointment> appointments = doctorService.getAppointmentsByDoctorId(email,false);
        model.addAttribute("doctor", doctor);
        model.addAttribute("appointments", appointments);
        return "DoctorDetailsForPatient";
    }

    @GetMapping("/{email}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable String email) {
        Patient patient = patientService.getPatientById(email);

        if (patient == null || patient.getProfilePictureContent() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Assuming it's JPEG, change if necessary

        return new ResponseEntity<>(patient.getProfilePictureContent(), headers, HttpStatus.OK);
    }
    @GetMapping("/{email}/profile-picture/doctor")
    public ResponseEntity<byte[]> getProfilePictureDoctor(@PathVariable String email) {
        Doctor doctor = doctorService.getDoctorById(email);

        if (doctor == null || doctor.getProfilePictureContent() == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG); // Assuming it's JPEG, change if necessary

        return new ResponseEntity<>(doctor.getProfilePictureContent(), headers, HttpStatus.OK);
    }
    @PostMapping("/{appointmentId}/cancel")
    public String deleteAppointment(@PathVariable Long appointmentId, @AuthenticationPrincipal UserDetails userDetails, Model model) {
        String patientUsername = userDetails.getUsername();
        if (patientUsername != null) {
            Patient patient = patientService.getPatientById(patientUsername);
            if (patient != null) {
                patientService.cancelAppointment(appointmentId, patient);
            }
        }
        Patient patient = patientService.getPatientById(patientUsername);
        List<Appointment> appointment = patientService.getAppointmentsByPatientEmail(patientUsername);
        model.addAttribute("patient", patient);
        model.addAttribute("appointments", appointment);
        List<Report> reports = patientService.getReportsForPatient(patientUsername);
        model.addAttribute("reports", reports);
        // Add other necessary attributes
        return "PatientAppointment";
    }
    @GetMapping("/{patientId}/reports")
    public String viewReports(@PathVariable String patientId, Model model) {
        List<Report> reports = patientService.getReportsForPatient(patientId);
        model.addAttribute("reports", reports);
        Patient patient = patientService.getPatientById(patientId);
        model.addAttribute("patient", patient);
        // Add other necessary attributes
        return "patient_dashboard";
    }

    @PostMapping("/book/{appointmentId}")
    public String bookAppointment(@PathVariable Long appointmentId, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        if (username != null) {
            Patient patient = patientService.getPatientById(username);
            boolean success = patientService.bookAppointment(appointmentId, patient);
            if (success) {
                return "bookingSuccess";
            } else {
                return "bookingFailure";
            }
        } else {
            return "loginPage";
        }
    }
    @GetMapping("/PatientAppointment")
    public String getPatientAppointment(@AuthenticationPrincipal UserDetails userDetails,Model model) {
        String username = userDetails.getUsername();
        if (username != null) {
            List<Appointment> appointment = patientService.getAppointmentsByPatientEmail(username);
            Patient patient = patientService.getPatientById(username);
            model.addAttribute("patient", patient);
            model.addAttribute("appointments", appointment);
            List<Report> reports = patientService.getReportsForPatient(username);
            return "PatientAppointment";
        } else {
            return "loginPage";
        }
    }

    @GetMapping("/EditProfile")
    public String EditProfile(@AuthenticationPrincipal UserDetails userDetails,Model model) {
        log.error("enter EditProfile");
        String username = userDetails.getUsername();
        if (username != null) {
            Patient patient = patientService.getPatientById(username);
            model.addAttribute("patient", patient);
            log.error("after EditProfile");
            return "EditPatientProfile";
        } else {
            return "loginPage";
        }
    }



}
