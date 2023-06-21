package com.netcracker.dataservice.service.auth;

import dto.AuthenticationDto;
import com.netcracker.dataservice.repositories.CustomerRepository;
import com.netcracker.dataservice.security.AuthenticationResponse;
import com.netcracker.dataservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final CustomerRepository customerRepository;
    private final JwtService jwtService;
    public AuthenticationResponse authenticate(AuthenticationDto authenticationDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationDto.getUsername(),
                        authenticationDto.getPassword()
                )
        );
        var customer = customerRepository.findByUsername(authenticationDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        customer.setLastLogin(LocalDateTime.now());
        customerRepository.save(customer);
        var jwtToken = jwtService.generateToken(customer);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
}
