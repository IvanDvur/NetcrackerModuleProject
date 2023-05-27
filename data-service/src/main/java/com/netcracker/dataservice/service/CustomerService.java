package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.repositories.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CustomerService {


    final CustomerRepository usuarioRepository;

    public CustomerService(CustomerRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public Optional<Customer> getByEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    public boolean existsEmail(String email){
        return usuarioRepository.existsByEmail(email);
    }

    public Customer save(Customer customer){
        return usuarioRepository.save(customer);
    }
}
