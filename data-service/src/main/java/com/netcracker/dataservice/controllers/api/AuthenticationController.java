package com.netcracker.dataservice.controllers.api;

import dto.AuthenticationDto;
import com.netcracker.dataservice.repositories.CustomerRepository;
import com.netcracker.dataservice.security.AuthenticationResponse;
import com.netcracker.dataservice.dto.RegistrationDto;
import com.netcracker.dataservice.service.auth.AuthenticationService;
import com.netcracker.dataservice.service.auth.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthenticationController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    private final CustomerRepository customerRepository;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegistrationDto registrationDto, Errors errors) {
        if (customerRepository.existsByEmail(registrationDto.getEmail()) ||
                customerRepository.existsByUsername(registrationDto.getUsername())) {
            return new ResponseEntity<>(new AuthenticationResponse(null), HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(registrationService.registerNewUserAccount(registrationDto));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Valid AuthenticationDto authenticationDto) {

        return ResponseEntity.ok(authenticationService.authenticate(authenticationDto));
    }
}

