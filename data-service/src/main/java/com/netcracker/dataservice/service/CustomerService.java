package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CustomerService {


    final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository usuarioRepository) {
        this.customerRepository = usuarioRepository;
    }

    public Optional<Customer> getByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    public boolean existsEmail(String email){
        return customerRepository.existsByEmail(email);
    }

    public Customer save(Customer customer){
        return customerRepository.save(customer);
    }

    public void updateLoginDate(LocalDateTime time){

    }
}
