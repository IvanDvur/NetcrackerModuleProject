package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.model.Role;
import com.netcracker.dataservice.repositories.CustomerRepository;
import com.netcracker.dataservice.security.AuthenticationResponse;
import com.netcracker.dataservice.security.JwtService;
import com.netcracker.dataservice.security.RegistrationForm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class RegistrationService{


    private final CustomerRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    public AuthenticationResponse registerNewUserAccount(RegistrationForm registrationForm){
        if (emailExists(registrationForm.getEmail())) {
            return null;
        }
        Customer customer = Customer
                .builder()
                .username(registrationForm.getUsername())
                .email(registrationForm.getEmail())
                .password(passwordEncoder.encode(registrationForm.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(customer);
        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    private boolean emailExists(String email) {
        return repository.findByEmail(email).isPresent();
    }
}

