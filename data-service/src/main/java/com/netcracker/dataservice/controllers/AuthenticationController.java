package com.netcracker.dataservice.controllers;

import com.netcracker.dataservice.security.AuthenticationForm;
import com.netcracker.dataservice.security.AuthenticationResponse;
import com.netcracker.dataservice.security.RegistrationForm;
import com.netcracker.dataservice.service.AuthenticationService;
import com.netcracker.dataservice.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@CrossOrigin
@RequiredArgsConstructor
public class AuthenticationController {

    private final RegistrationService registrationService;
    private final AuthenticationService authenticationService;
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegistrationForm registrationForm,Errors errors){
//        if(errors.hasErrors()){
//            return new ResponseEntity<>();
//        }
      return ResponseEntity.ok(registrationService.registerNewUserAccount(registrationForm));
    }

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid AuthenticationForm authenticationForm){

        return ResponseEntity.ok(authenticationService.authenticate(authenticationForm));
    }
}
