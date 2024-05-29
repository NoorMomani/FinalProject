package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.*;
import com.example.mentalhealthsystem.constants.DoctorStatus;
import com.example.mentalhealthsystem.repository.AppointmentRepository;
import com.example.mentalhealthsystem.repository.DoctorRepository;
import com.example.mentalhealthsystem.request.DoctorSignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private LocationService locationService;

    @Mock
    private JavaMailSender emailSender;
    @Mock
    LocalDateTime appointmentDateTime;

    @InjectMocks
    private DoctorService doctorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateDoctorProfile () throws IOException {
        String email = "test";
        Doctor doctor = new Doctor();
        doctor.setEmail(email);

        DoctorSignUpRequest doctorSUR = new DoctorSignUpRequest();
        doctorSUR.setName("Dr. Smith");
        doctorSUR.setLocation("40.7128, -74.0060");

        MultipartFile profilePicture = mock(MultipartFile.class);
        when(profilePicture.isEmpty()).thenReturn(false);
        when(profilePicture.getBytes()).thenReturn(new byte[]{1, 2, 3});
        doctorSUR.setProfilePicture(profilePicture);
        when(doctorRepository.findById(email)).thenReturn(Optional.of(doctor));

        doctorService.updateDoctorProfile(email, doctorSUR);

        assertNotNull(doctor.getProfilePictureContent());
    }



    @Test
    void getDoctorByIdWhenIdInDBThenReturnDoctorObject() {
        Doctor doctor = new Doctor();
        when(doctorRepository.findById("test")).thenReturn(Optional.of(doctor));
        assertEquals(doctor,doctorService.getDoctorById("test"));
    }
    @Test
    void getDoctorByIdWhenIdNotInDBThenReturnNull() {
        assertNull(doctorService.getDoctorById("test"));
    }

    @Test
    void deleteAppointment() {
        Appointment appointment = new Appointment();
        Doctor doctor = new Doctor();
        doctor.setEmail("test");
        appointment.setBooked(true);
        appointment.setDoctor(doctor);
        Patient patient = new Patient();
        patient.setEmail("test1");
        appointment.setPatient(patient);

        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        doctorService.deleteAppointment("test",1L);
        verify(appointmentRepository).delete(any(Appointment.class));
    }

    @Test
    void addAppointmentWhenDoctorNotFoundThenDoNothing() {
        when(doctorRepository.findById("test")).thenReturn(Optional.empty());
        doctorService.addAppointment("test", appointmentDateTime);
        verify(appointmentRepository, never()).save(any(Appointment.class));
    }

    @Test
    void addAppointment() {
        Doctor doctor = new Doctor();
        when(doctorRepository.findById("test")).thenReturn(Optional.of(doctor));
        doctorService.addAppointment("test", appointmentDateTime);
        verify(appointmentRepository).save(any(Appointment.class));
    }

    @Test
    void getAllDoctors() {
        Doctor doctor = new Doctor();
        List<Doctor> listDoctor = List.of(doctor);
        Optional<List<Doctor>> optionalDoctors = Optional.of(listDoctor);
        doReturn(optionalDoctors).when(doctorRepository).findAllByStatus(DoctorStatus.APPROVED);
        assertEquals(listDoctor,doctorService.getAllDoctors());
    }

    @Test
    void getAllDoctorsWhenNoDoctorsReturnEmptyList() {
        Optional<List<Doctor>> optionalDoctors = Optional.empty();
        doReturn(optionalDoctors).when(doctorRepository).findAllByStatus(DoctorStatus.APPROVED);
        assertEquals(List.of(),doctorService.getAllDoctors());
    }

    @Test
    void save() {
        Doctor doctor = new Doctor();
        doctor.setEmail("test");
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        doctorService.save(doctor);

        verify(doctorRepository).save(doctor);
    }

    @Test
    void getAppointmentsByDoctorId() {
        Appointment appointment = new Appointment();
        List<Appointment> appointments = List.of(appointment);
        when(appointmentRepository.findByDoctorEmailAndBooked("test",true)).thenReturn(appointments);
        assertEquals(appointments,doctorService.getAppointmentsByDoctorId("test",true));
    }

    @Test
    void testGetAppointmentsByDoctorId() {
        Appointment appointment = new Appointment();
        List<Appointment> appointments = List.of(appointment);
        when(appointmentRepository.findByDoctorEmail("test")).thenReturn(appointments);
        assertEquals(appointments,doctorService.getAppointmentsByDoctorId("test"));
    }
}