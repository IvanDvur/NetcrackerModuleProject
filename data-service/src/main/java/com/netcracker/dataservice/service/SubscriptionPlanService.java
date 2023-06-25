package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.model.Role;
import com.netcracker.dataservice.repositories.CustomerRepository;
import com.netcracker.dataservice.security.AuthenticationResponse;
import com.netcracker.dataservice.security.JwtService;
import dto.TokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {


    private final JwtService jwtService;
    private final CustomerRepository customerRepository;

    public ResponseEntity<TokenDto> changeCustomerPaymentPlan(String token, String plan) {
        Customer customer = getCustomerFromJwt(token);
        if(customer!=null){
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("role",plan);
            String responseToken = jwtService.generateToken(extraClaims,customer);
            customer.setRole(Role.valueOf(plan));
            customerRepository.save(customer);
            TokenDto tokenDto = new TokenDto();
            tokenDto.setValue(responseToken);
            return new ResponseEntity<>(tokenDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    private Customer getCustomerFromJwt(String token) {
        String jwt = token.substring(7);
        String customerUsername = jwtService.extractUsername(jwt);
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(customerUsername);
        return optionalCustomer.orElse(null);
    }
}
