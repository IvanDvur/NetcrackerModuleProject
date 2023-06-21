package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.model.Role;
import com.netcracker.dataservice.repositories.CustomerRepository;
import com.netcracker.dataservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanService {


    private final JwtService jwtService;
    private final CustomerRepository customerRepository;

    public void changeCustomerPaymentPlan(String token, String plan) {
        Customer customer = getCustomerFromJwt(token);
        if(customer!=null){
            customer.setRole(Role.valueOf(plan));
        }
    }


    private Customer getCustomerFromJwt(String token) {
        String jwt = token.substring(7);
        String customerUsername = jwtService.extractUsername(jwt);
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(customerUsername);
        return optionalCustomer.orElse(null);
    }
}
