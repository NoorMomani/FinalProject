package com.example.mentalhealthsystem.Handlers;

import com.example.mentalhealthsystem.constants.UserRoles;
import com.example.mentalhealthsystem.service.ValidatorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
public class AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final ValidatorService validatorService;

    public AuthenticationSuccessHandler(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        boolean isDoctor = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> UserRoles.DOCTOR.equals(UserRoles.getEnumByValue(grantedAuthority.getAuthority())));
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> UserRoles.ADMIN.equals(UserRoles.getEnumByValue(grantedAuthority.getAuthority())));
        if (isAdmin){
            UserDetails admin = (UserDetails) authentication.getPrincipal();
            if (validatorService.isActiveAdmin(admin.getUsername())) {
                setDefaultTargetUrl("/admins/dashboard");
            } else {
                setDefaultTargetUrl("/login_failed");
            }
        } else if(isDoctor){
            UserDetails doctor = (UserDetails) authentication.getPrincipal();
            if (validatorService.isActiveDoctor(doctor.getUsername())) {
                setDefaultTargetUrl("/doctors/dashboard");
            } else {
                setDefaultTargetUrl("/login_failed");
            }
        }

        else
        setDefaultTargetUrl("/homepage");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
