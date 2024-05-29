package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.*;
import com.example.mentalhealthsystem.repository.AppointmentRepository;
import com.example.mentalhealthsystem.repository.PatientRepository;
import com.example.mentalhealthsystem.repository.ReportRepository;
import com.example.mentalhealthsystem.request.PatientSignUpRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private ReportRepository reportRepository;
    @Autowired
    private LocationService locationService;
    @PersistenceContext
    private EntityManager entityManager;

    private final JavaMailSender emailSender;
    public PatientService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public Patient getPatientById(String id) {
        return patientRepository.findById(id).orElse(null);
    }

    public void updatePatientProfile(String email, PatientSignUpRequest patientSUR) {
        Patient patient = patientRepository.findById(email).orElse(null);
        if (patient != null) {
            updatePatientProfileFields(patient, patientSUR);
            handleProfilePictureUpload(patient, patientSUR.getProfilePicture());
            patientRepository.save(patient);
        } else {
            throw new RuntimeException("Patient not found with email: " + email);
        }
    }
    private void updatePatientProfileFields(Patient patient, PatientSignUpRequest patientSUR) {
        patient.setName(patientSUR.getName());
        patient.setNationality(patientSUR.getNationality());
        patient.setPhoneNumber(patientSUR.getPhoneNumber());
        patient.setGender(patientSUR.getGender());
        patient.setAge(patientSUR.getAge());
        patient.setCountry(patientSUR.getCountry());
        patient.setCity(patientSUR.getCity());
    }

    private void handleProfilePictureUpload(Patient patient, MultipartFile profilePicture) {
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                patient.setProfilePictureContent(profilePicture.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload profile picture", e);
            }
        }
    }

    @Transactional
    public boolean bookAppointment(Long appointmentId, Patient patient) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null && !appointment.isBooked()) {
            appointment.setPatient(patient);
            appointment.setBooked(true);
            appointmentRepository.save(appointment);
            return true;
        }
        return false;
    }
    public List<Appointment> getAppointmentsByPatientEmail(String email) {
        return appointmentRepository.findByPatient_Email(email);
    }

    public List<Appointment> getAppointmentsByDoctorEmailAndBookedIsTrue(String doctorEmail) {
        return appointmentRepository.findByDoctorEmailAndBooked(doctorEmail, true);
    }

    @Scheduled(fixedRate = 3600000) // Runs every hour
    public void deleteExpiredAppointments() {
        List<Appointment> expiredAppointments = appointmentRepository.findAll().stream()
                .filter(appointment -> appointment.getSession().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
        appointmentRepository.deleteAll(expiredAppointments);
    }

    @Transactional
    public void sendConfirmationEmail(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        log.error("sendConfirmationEmail appointment {}", appointment.getId());
        if (appointment != null && appointment.isBooked()) {
            Patient patient = appointment.getPatient();
            if (patient != null && patient.getEmail() != null) {
                log.error("sendConfirmationEmail appointment 3 {}", appointment.getDoctor());

                // Retrieve the clinic location
                Location clinicLocation = locationService.getLocationByDoctor(appointment.getDoctor());
                log.error("clinicLocation appointment {}", clinicLocation.getId());

                // Construct the URL for viewing the location on a map
                String mapUrl = "https://maps.google.com/?q=" + clinicLocation.getLatitude() + "," + clinicLocation.getLongitude();

                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(patient.getEmail());
                message.setSubject("Appointment Confirmation");
                message.setText("Dear " + patient.getName() + ",\n\n" +
                        "Your appointment with Dr. " + appointment.getDoctor().getName() + " on " +
                        appointment.getSession() + " has been confirmed.\n\n" +

                        "You can view the location on map by clicking on the following link: " + mapUrl + "\n\n" +
                        "We look forward to seeing you.\n\n" +
                        "Best regards,\nYour Clinic");
                emailSender.send(message);
            }
        }
    }


    public void cancelAppointment(Long appointmentId, Patient patient) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null && appointment.getPatient().getEmail().equals(patient.getEmail())) {
            Doctor doctor = appointment.getDoctor();
            appointment.setBooked(false);
            appointment.setPatient(null);
            appointmentRepository.save(appointment);

            // Send email notification to the doctor
            if (doctor != null && doctor.getEmail() != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(doctor.getEmail());
                message.setSubject("Appointment Cancellation Notification");
                message.setText("Dear Dr. " + doctor.getName() + ",\n\n" +
                        "Your appointment with patient " + patient.getName() + " has been cancelled.\n\n" +
                        "Best regards,\nYour Clinic");
                emailSender.send(message);
            }
        }
    }
    public void addReport(Long appointmentId, String reportContent) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null && appointment.isBooked()) {
            Report report = new Report(reportContent, appointment);
            reportRepository.save(report);

            Patient patient = appointment.getPatient();
            if (patient != null && patient.getEmail() != null) {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(patient.getEmail());
                message.setSubject("New Report from Dr. " + appointment.getDoctor().getName());
                message.setText("Dear " + patient.getName() + ",\n\n" +
                        "Dr. " + appointment.getDoctor().getName() + " has written a new report for you. " +
                        "Please log in to your account to view the report.\n\n" +
                        "Best regards,\n"+
                        "Your Clinic");
                emailSender.send(message);
            }
        }
    }
    public List<Report> getReportsForPatient(String patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatient_Email(patientId);
        return reportRepository.findByAppointmentIn(appointments);
    }
}
