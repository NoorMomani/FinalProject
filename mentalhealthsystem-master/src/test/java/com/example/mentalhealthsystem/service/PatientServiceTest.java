package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.*;
import com.example.mentalhealthsystem.repository.AppointmentRepository;
import com.example.mentalhealthsystem.repository.PatientRepository;
import com.example.mentalhealthsystem.repository.ReportRepository;
import com.example.mentalhealthsystem.request.PatientSignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getPatientByIdWhenIdInDBThenReturnPatientObject() {
        Patient patient = new Patient();
        doReturn(Optional.of(patient)).when(patientRepository).findById("test");
        assertEquals(patient, patientService.getPatientById("test"));
    }

    @Test
    void getPatientByIdWhenIdNotInDBThenReturnNull() {
        assertNull(patientService.getPatientById("test"));
    }


    @Test
    void updatePatientProfile() {
        Patient patient = new Patient();
        PatientSignUpRequest patientSUR = new PatientSignUpRequest();

        doReturn(Optional.of(patient)).when(patientRepository).findById("test");
        when(patientRepository.save(patient)).thenReturn(patient);

        patientService.updatePatientProfile("test", patientSUR);
        verify(patientRepository, times(1)).save(patient);

    }

    @Test
    void bookAppointmentWhenAppointmentIsBooked() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setBooked(false);

        Patient patient = new Patient();
        patient.setEmail("patient@example.com");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        boolean result = patientService.bookAppointment(1L, patient);
        assertTrue(result);
    }
    @Test
    void bookAppointmentWhenAppointmentIsNotBooked() {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setBooked(false);

        Patient patient = new Patient();
        patient.setEmail("patient@example.com");

        when(appointmentRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = patientService.bookAppointment(1L, patient);
        assertFalse(result);
    }

    @Test
    void getAppointmentsByPatientEmail() {
        Appointment appointment = new Appointment();
        List<Appointment> appointmentList = List.of(appointment) ;
        doReturn(appointmentList).when(appointmentRepository).findByPatient_Email("test");
        assertEquals(appointmentList, patientService.getAppointmentsByPatientEmail("test"));
    }

    @Test
    void getAppointmentsByDoctorEmailAndBookedIsTrue() {
        Appointment appointment = new Appointment();
        List<Appointment> appointmentList = List.of(appointment) ;
        doReturn(appointmentList).when(appointmentRepository).findByDoctorEmailAndBooked("test",true);
        assertEquals(appointmentList, patientService.getAppointmentsByDoctorEmailAndBookedIsTrue("test"));
    }

    @Test
    void deleteExpiredAppointments() {
        Appointment expiredAppointment1 = new Appointment();
        expiredAppointment1.setSession(LocalDateTime.now().minusHours(2));

        List<Appointment> allAppointments = new ArrayList<>();
        allAppointments.add(expiredAppointment1);
        when(appointmentRepository.findAll()).thenReturn(allAppointments);

        patientService.deleteExpiredAppointments();
        verify(appointmentRepository, times(1)).deleteAll(anyList());


    }

    @Test
    void cancelAppointment() {
        Doctor doctor = new Doctor();
        doctor.setEmail("doctor@example.com");
        doctor.setName("Dr. Smith");

        Patient patient = new Patient();
        patient.setEmail("patient@example.com");
        patient.setName("John Doe");
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setBooked(true);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        patientService.cancelAppointment(1L, patient);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void addReport() {
        Patient patient = new Patient();
        patient.setEmail("patient@example.com");
        patient.setName("John Doe");

        Doctor doctor = new Doctor();
        doctor.setName("Dr. Smith");

        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setBooked(true);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        String reportContent = "This is a report content.";

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        patientService.addReport(1L, reportContent);

        verify(reportRepository, times(1)).save(any(Report.class));

    }

    @Test
    void getReportsForPatient() {
        Appointment appointment = new Appointment();
        appointment.setBooked(true);
        appointment.setId(1L);
        List<Appointment> appointmentList = List.of(appointment);
        Report report = new Report();
        List<Report> reports = List.of(report);
        doReturn(appointmentList).when(appointmentRepository).findByPatient_Email("test");
        doReturn(reports).when(reportRepository).findByAppointmentIn(appointmentList);
        assertEquals(reports, patientService.getReportsForPatient("test"));
    }
}