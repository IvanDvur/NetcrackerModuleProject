package com.netcracker.dataservice.service;

import com.netcracker.dataservice.repositories.CustomerRepository;
import com.netcracker.dataservice.security.AuthenticationForm;
import com.netcracker.dataservice.security.AuthenticationResponse;
import com.netcracker.dataservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    public AuthenticationResponse authenticate(AuthenticationForm authenticationForm) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationForm.getUsername(),
                        authenticationForm.getPassword()
                )
        );
        var customer = customerRepository.findByUsername(authenticationForm.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
