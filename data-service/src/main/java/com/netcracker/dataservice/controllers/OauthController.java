package com.netcracker.dataservice.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.netcracker.dataservice.dto.TokenDto;
import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.model.Role;
import com.netcracker.dataservice.security.JwtService;
import com.netcracker.dataservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/oauth")
@CrossOrigin
public class OauthController {

    @Value("427449141788-volpt3j75dicq8kpajve17vbe1aqqnb0.apps.googleusercontent.com")
    String googleClientId;

    @Value("kasdjhfkadhsY776ggTyUU65khaskdjfhYuHAwjñlji")
    String secretPsw;


    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtProvider;

    @Autowired
    CustomerService usuarioService;




    @PostMapping("/google")
    public ResponseEntity<TokenDto> google(@RequestBody TokenDto tokenDto) throws IOException {
        final NetHttpTransport transport = new NetHttpTransport();

        final JsonFactory jacksonFactory = GsonFactory.getDefaultInstance();

        GoogleIdTokenVerifier.Builder verifier =
                new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                        .setAudience(Collections.singletonList(googleClientId));
        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();
        Customer user = new Customer();
        if(usuarioService.existsEmail(payload.getEmail()))
            user = usuarioService.getByEmail(payload.getEmail()).get();
        else
            user = saveUsuario(payload.getEmail());
        TokenDto tokenRes = login(user);
        return new ResponseEntity(tokenRes, HttpStatus.OK);
    }

    private TokenDto login(Customer customer){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(customer.getEmail(), secretPsw)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(customer);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setValue(jwt);
        return tokenDto;
    }
    private Customer saveUsuario(String email){
//        Customer customer = new Customer();
//        customer.setUsername(email);
//        customer.setPassword(passwordEncoder.encode(secretPsw));

        Customer customer = Customer
                .builder()
                .username(email)
                .email(email)
                .password(passwordEncoder.encode(secretPsw))
                .role(Role.USER)
                .build();

        return usuarioService.save(customer);
    }

}