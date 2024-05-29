package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.Appointment;
import com.example.mentalhealthsystem.Database.Doctor;
import com.example.mentalhealthsystem.Database.Patient;
import com.example.mentalhealthsystem.constants.DoctorStatus;
import com.example.mentalhealthsystem.repository.AppointmentRepository;
import com.example.mentalhealthsystem.repository.DoctorRepository;
import com.example.mentalhealthsystem.request.DoctorSignUpRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private LocationService locationService;
    private final JavaMailSender emailSender;

    public DoctorService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public Doctor getDoctorById(String id) {
        return doctorRepository.findById(id).orElse(null);
    }

    public void updateDoctorProfile(String email, DoctorSignUpRequest doctorSUR) throws IOException {
        Doctor doctor = doctorRepository.findById(email).orElse(null);
        if (doctor != null) {
            log.error("befor updateDoctorProfileFields");
            updateDoctorProfileFields(doctor, doctorSUR);
            log.error("after updateDoctorProfileFields");
            handleFileUploads(doctor, doctorSUR);
            log.error("after handleFileUploads");
            updateDoctorLocation(doctor, doctorSUR.getLocation());
            log.error("after updateDoctorLocation");
            doctorRepository.save(doctor);
            log.error("after doctorRepository.save(doctor);");
        } else {
            throw new RuntimeException("Doctor not found with email: " + email);
        }
    }

    private void updateDoctorProfileFields(Doctor doctor, DoctorSignUpRequest doctorSUR) {
        doctor.setName(doctorSUR.getName());
        doctor.setYearsOfExperience(doctorSUR.getYearsOfExperience());
        doctor.setCountry(doctorSUR.getCountry());
        doctor.setCity(doctorSUR.getCity());
        doctor.setJobTitle(doctorSUR.getJobTitle());
        doctor.setNationality(doctorSUR.getNationality());
        doctor.setPhoneNumber(doctorSUR.getPhoneNumber());
        doctor.setAddress(doctorSUR.getAddress());
        doctor.setGender(doctorSUR.getGender());
        doctor.setAge(doctorSUR.getAge());
        doctor.setSessionPrice(doctorSUR.getSessionPrice());
        doctor.setAboutTheDoctor(doctorSUR.getAboutTheDoctor());
    }

    private void handleFileUploads(Doctor doctor, DoctorSignUpRequest doctorSUR) throws IOException {

        MultipartFile cv = doctorSUR.getCv();
        if (cv != null && !cv.isEmpty()) {
            doctor.setCvFileName(cv.getOriginalFilename());
            doctor.setCvContent(cv.getBytes());
            doctor.setCvContentType(cv.getContentType());
        }


        MultipartFile profilePicture = doctorSUR.getProfilePicture();
        log.error("profilePicture != null {}", profilePicture != null);
        log.error("!profilePicture.isEmpty() {}", !profilePicture.isEmpty());
        if (profilePicture != null && !profilePicture.isEmpty()) {
            doctor.setProfilePictureContent(profilePicture.getBytes());
        }


        MultipartFile identityLicense = doctorSUR.getIdentityLicense();
        if (identityLicense != null && !identityLicense.isEmpty()) {
            doctor.setIdentityLicenseFileName(identityLicense.getOriginalFilename());
            doctor.setIdentityLicenseContent(identityLicense.getBytes());
            doctor.setIdentityLicenseContentType(identityLicense.getContentType());
        }
    }


    private void updateDoctorLocation(Doctor doctor, String locationString) {
        if (locationString != null && !locationString.isEmpty()) {
            String[] latLng = locationString.split(", ");
            double latitude = Double.parseDouble(latLng[0]);
            double longitude = Double.parseDouble(latLng[1]);
            locationService.updateLocation(latitude, longitude, doctor);
        }
    }




    public void updateAppointment(String doctorId, Long appointmentId,
                                  LocalDateTime session) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null && appointment.getDoctor().getEmail().equals(doctorId)) {
            boolean wasBooked = appointment.isBooked();
            appointment.setSession(session);
            appointmentRepository.save(appointment);


            if (wasBooked) {
                Patient patient = appointment.getPatient();
                if (patient != null && patient.getEmail() != null) {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(patient.getEmail());
                    message.setSubject("Appointment Update Notification");
                    message.setText("Dear " + patient.getName() + ",\n\n" +
                            "Your appointment with Dr. " + appointment.getDoctor().getName() + " has been updated.\n" +
                            "New Appointment Date and Time: " + appointment.getSession() + "\n\n" +
                            "We apologize for any inconvenience.\n\n" +
                            "Best regards,\nYour Clinic");
                    emailSender.send(message);
                }
            }
        }
    }

    public void deleteAppointment(String doctorId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElse(null);
        if (appointment != null && appointment.getDoctor().getEmail().equals(doctorId)) {
            if (appointment.isBooked()) {
                Patient patient = appointment.getPatient();
                if (patient != null && patient.getEmail() != null) {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(patient.getEmail());
                    message.setSubject("Appointment Cancellation Notice");
                    message.setText("Dear " + patient.getName() + ",\n\n" +
                            "We regret to inform you that your appointment with Dr. " +
                            appointment.getDoctor().getName() + " on " +
                            appointment.getSession() + " has been cancelled.\n\n" +
                            "We apologize for any inconvenience caused.\n\n" +
                            "Best regards,\nYour Clinic");
                    emailSender.send(message);
                }
            }
            appointmentRepository.delete(appointment);
        }
    }

    public void addAppointment(String doctorId, LocalDateTime appointmentDateTime) {
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor != null) {
            Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setSession(appointmentDateTime);
            appointment.setBooked(false);
            appointmentRepository.save(appointment);
        }
    }

    public List<Doctor> getAllDoctors()
    {
        return doctorRepository.findAllByStatus(DoctorStatus.APPROVED).orElse(List.of());
    }
    public Doctor findByEmail(String email)
    {
        return doctorRepository.findById(email).orElse(null);
    }
    public void save(Doctor doctor) {
        doctorRepository.save(doctor);
    }
    public List<Appointment> getAppointmentsByDoctorId(String doctorId, boolean booked) {
        return appointmentRepository.findByDoctorEmailAndBooked(doctorId, booked);
    }
    private Appointment mapRowToAppointment(ResultSet rs, int rowNum) throws SQLException {
        Appointment appointment = new Appointment();
        appointment.setId(rs.getLong("id"));
        appointment.setSession(rs.getObject("session", LocalDateTime.class));
        return appointment;
    }

    public List<Appointment> getAppointmentsByDoctorId(String doctorId) {
        return appointmentRepository.findByDoctorEmail(doctorId);
    }
}
