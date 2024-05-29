package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.UserLoginDetails;
import com.example.mentalhealthsystem.constants.UserRoles;
import com.example.mentalhealthsystem.repository.UserLoginDetailsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class MentalHealthUserDetailsServiceTest {

    @Mock
    UserLoginDetailsRepository userLoginDetailsRepository;

    @InjectMocks
    MentalHealthUserDetailsService mentalHealthUserDetailsService;
    @Test
    void loadUserByUsernameWhenUserInDBThenReturnUserDetailsObject() {
        UserLoginDetails userLoginDetails = new UserLoginDetails();
        userLoginDetails.setEmail("test@example.com");
        userLoginDetails.setPassword("123");
        userLoginDetails.setRole(UserRoles.PATIENT);
        doReturn(Optional.of(userLoginDetails)).when(userLoginDetailsRepository).findByEmail("test@example.com");
        UserDetails userDetails = User.builder()
                .username("test@example.com")
                .password("123")
                .roles(UserRoles.PATIENT.name())
                .build();
        assertEquals(userDetails, mentalHealthUserDetailsService.loadUserByUsername("test@example.com"));
      }

    @Test
    void getQuestionsByAssessmentIdWhenIdNotInDBThenReturnNull() {
        UserLoginDetails userLoginDetails = new UserLoginDetails();
        assertThrows(UsernameNotFoundException.class, () ->mentalHealthUserDetailsService.loadUserByUsername("test"));
    }
}