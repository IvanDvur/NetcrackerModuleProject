package com.netcracker.dataservice.service.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.model.Role;
import com.netcracker.dataservice.security.JwtService;
import com.netcracker.dataservice.service.CustomerService;
import dto.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleAuthService {
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
    CustomerService customerService;

    public ResponseEntity<TokenDto> authorize(TokenDto tokenDto) {
        final NetHttpTransport transport = new NetHttpTransport();
        final JsonFactory jacksonFactory = GsonFactory.getDefaultInstance();
        try {
            GoogleIdTokenVerifier.Builder verifier =
                    new GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                            .setAudience(Collections.singletonList(googleClientId));
            final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), tokenDto.getValue());
            final GoogleIdToken.Payload payload = googleIdToken.getPayload();
            Customer user;
            if (customerService.existsEmail(payload.getEmail())) {
                user = customerService.getByEmail(payload.getEmail()).get();
                user.setLastLogin(LocalDateTime.now());
                customerService.save(user);
            } else {
                user = saveUser(payload.getEmail());
            }
            TokenDto tokenRes = login(user);
            return new ResponseEntity(tokenRes, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private TokenDto login(Customer customer) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(customer.getEmail(), secretPsw)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Map<String,Object> extraClaims = new HashMap<>();
        extraClaims.put("role",customer.getRole());
        String jwt = jwtProvider.generateToken(extraClaims,customer);
        TokenDto tokenDto = new TokenDto();
        tokenDto.setValue(jwt);
        return tokenDto;
    }

    private Customer saveUser(String email) {
        Customer customer = Customer
                .builder()
                .username(email)
                .email(email)
                .password(passwordEncoder.encode(secretPsw))
                .role(Role.USER)
                .lastLogin(LocalDateTime.now())
                .build();
        return customerService.save(customer);
    }
}
