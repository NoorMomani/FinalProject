package com.example.mentalhealthsystem.request;

import com.example.mentalhealthsystem.Database.Appointment;
import com.example.mentalhealthsystem.constants.Language;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DoctorSignUpRequest extends SignUpRequest{
    private String name;

    private String DateOfBirth;

    private String nationality;

    private String phoneNumber;

    private String address;

    private String gender;

    private int age;

    private String country;

    private String city;
    private MultipartFile profilePicture;


    private Set<Language> language;
    int yearsOfExperience;
    private String jobTitle;

    private String cvFileName;

    private byte[] cvContent;

    private String cvContentType;
    private MultipartFile cv;

    private String identityLicenseFileName;

    private byte[] identityLicenseContent;

    private String identityLicenseContentType;
    private MultipartFile identityLicense;

    private int sessionPrice;
    private String aboutTheDoctor;
    List<Appointment> appointments;
    private String location;
}
