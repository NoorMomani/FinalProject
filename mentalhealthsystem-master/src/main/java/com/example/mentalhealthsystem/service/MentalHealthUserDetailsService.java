package com.example.mentalhealthsystem.service;

import com.example.mentalhealthsystem.Database.UserLoginDetails;
import com.example.mentalhealthsystem.repository.UserLoginDetailsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MentalHealthUserDetailsService implements UserDetailsService {
    @Autowired
    UserLoginDetailsRepository userLoginDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserLoginDetails> optionalUser = userLoginDetailsRepository.findByEmail(username);
        if (optionalUser.isPresent()){
            UserLoginDetails user = optionalUser.get();
            return User.builder().username(user.getEmail())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();

        } else{
            log.error("couldn't find username");
            throw new UsernameNotFoundException("couldn't find user for email: " + username);
        }
    }


}
