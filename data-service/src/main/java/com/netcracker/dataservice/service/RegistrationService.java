package com.netcracker.dataservice.service;


import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.model.Role;
import com.netcracker.dataservice.repositories.CustomerRepository;
import com.netcracker.dataservice.security.AuthenticationResponse;
import com.netcracker.dataservice.security.JwtService;
import com.netcracker.dataservice.dto.RegistrationDto;
import lombok.RequiredArgsConstructor;
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
    public AuthenticationResponse registerNewUserAccount(RegistrationDto registrationDto) {

        Customer customer = Customer
                .builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .role(Role.USER)
                .build();
        repository.save(customer);
        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

}

