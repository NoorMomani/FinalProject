package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.*;
import com.example.mentalhealthsystem.constants.DoctorStatus;
import com.example.mentalhealthsystem.repository.AdminRepository;
import com.example.mentalhealthsystem.repository.DoctorRepository;
import com.example.mentalhealthsystem.repository.UserLoginDetailsRepository;
import com.example.mentalhealthsystem.request.AdminSignUpRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {
    @Mock
    private AdminRepository adminRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserLoginDetailsRepository userLoginDetailsRepository;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updatePassword() {
        String email = "test";
        String newPassword = "newPassword123";
        String encodedPassword = "encodedPassword123";
        UserLoginDetails userLoginDetails = new UserLoginDetails();
        userLoginDetails.setEmail(email);
        userLoginDetails.setPassword("oldPassword");

        when(userLoginDetailsRepository.findByEmail(email)).thenReturn(Optional.of(userLoginDetails));
        adminService.updatePassword(email, newPassword);
    }
    @Test
    void updatePasswordWhenUserDoesNotExist() {
        String email = "test";
        String newPassword = "newPassword123";

        when(userLoginDetailsRepository.findByEmail(email)).thenReturn(Optional.empty());

       assertThrows(UsernameNotFoundException.class, () -> {
            adminService.updatePassword(email, newPassword);
        });

    }

    @Test
    void getAdminByIdWhenIdInDBThenReturnAdminObject() {
        Admin admin = new Admin();
        doReturn(Optional.of(admin)).when(adminRepository).findById("test");
        assertEquals(admin, adminService.getAdminById("test"));
    }
    @Test
    void getAdminByIdWhenIdNotInDBThenReturnNull() {
        assertNull(adminService.getAdminById("test"));
    }

    @Test
    void getRequestedDoctors() {
        Doctor doctor = new Doctor();
        List<Doctor> doctors = List.of(doctor);
        Optional<List<Doctor>> optionalDoctors= Optional.of(doctors);
        doReturn(optionalDoctors).when(doctorRepository).findAllByStatus(DoctorStatus.PENDING);
        assertEquals(doctors, adminService.getRequestedDoctors());
    }

    @Test
    void findPendingDoctorByEmail() {
        Doctor doctor = new Doctor();
        doctor.setEmail("test@example.com");
        Optional<Doctor> optionalDoctor = Optional.of(doctor);
        doReturn(optionalDoctor).when(doctorRepository).findByEmailAndStatus("test@example.com",DoctorStatus.PENDING);
        assertEquals(optionalDoctor,adminService.findPendingDoctorByEmail("test@example.com"));
    }

    @Test
    void findApprovedDoctorByEmail() {
        Doctor doctor = new Doctor();
        doctor.setEmail("test@example.com");
        Optional<Doctor> optionalDoctor = Optional.of(doctor);
        doReturn(optionalDoctor).when(doctorRepository).findByEmailAndStatus("test@example.com",DoctorStatus.APPROVED);
        assertEquals(optionalDoctor,adminService.findApprovedDoctorByEmail("test@example.com"));
    }

    @Test
    void getSystemDoctors() {
        Doctor doctor = new Doctor();
        List<Doctor> doctors = List.of(doctor);
        Optional<List<Doctor>> optionalDoctors= Optional.of(doctors);
        doReturn(optionalDoctors).when(doctorRepository).findAllByStatus(DoctorStatus.APPROVED);
        assertEquals(doctors, adminService.getSystemDoctors());
    }

    @Test
    void getDoctorByEmailDoctorFound() {
        Doctor doctor = new Doctor();
        doctor.setEmail("test@example.com");
        when(doctorRepository.findById("test@example.com")).thenReturn(Optional.of(doctor));
        assertEquals(doctor, adminService.getDoctorByEmail("test@example.com"));
    }
    @Test
    void getDoctorByEmailDoctorNotFound() {
        when(doctorRepository.findById("test@example.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> {
            adminService.getDoctorByEmail("test@example.com");
        });

    }

    @Test
    void acceptDoctors() {
        Doctor doctor = new Doctor();
        doctor.setEmail("test");
        doctor.setStatus(DoctorStatus.PENDING);

        when(doctorRepository.findById("test")).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        adminService.acceptDoctors("test");
        verify(doctorRepository).save(doctor);

    }

    @Test
    void deleteDoctor() {
        String email = "test@example.com";
        adminService.deleteDoctor(email);
        verify(doctorRepository, times(1)).deleteById(email);
    }


    @Test
    void rejectDoctors() {
        String email = "test@example.com";
        Doctor doctor = new Doctor();
        doctor.setEmail(email);
        doctor.setStatus(DoctorStatus.PENDING);

        when(doctorRepository.findById(email)).thenReturn(Optional.of(doctor));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);

        assertEquals(DoctorStatus.DECLINED, adminService.rejectDoctors(email).getStatus());
    }


    @Test
    void changePassword() {
        String email = "test";
        String newPassword = "newPassword123";
        UserLoginDetails userLoginDetails = new UserLoginDetails();
        userLoginDetails.setEmail(email);
        userLoginDetails.setPassword("oldPassword");

        when(userLoginDetailsRepository.findByEmail(email)).thenReturn(Optional.of(userLoginDetails));
        adminService.changePassword(email, newPassword);

        assertEquals(newPassword, userLoginDetails.getPassword());
    }

    @Test
    void updateAdminWhenExisting() {
        String email = "test@example.com";
        Admin admin = new Admin();
        admin.setEmail(email);

        AdminSignUpRequest adminSUR = new AdminSignUpRequest();
        adminSUR.setName("Test Name");
        adminSUR.setNationality("Test Nationality");
        adminSUR.setPhoneNumber("123456789");
        adminSUR.setGender("Male");
        adminSUR.setAge(30);
        adminSUR.setCountry("Test Country");
        adminSUR.setCity("Test City");

        when(adminRepository.findById(email)).thenReturn(Optional.of(admin));
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        adminService.updateAdmin(email, adminSUR);
        verify(adminRepository).save(admin);

    }
    @Test
    void updateAdminWhenNonExistingAdmin() {
        AdminSignUpRequest adminSUR = new AdminSignUpRequest();
        when(adminRepository.findById("test")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                adminService.updateAdmin("test", adminSUR));

    }

}