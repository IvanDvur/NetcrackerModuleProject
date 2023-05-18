package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Client;
import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.model.MailingList;
import com.netcracker.dataservice.repositories.CustomerRepository;
import com.netcracker.dataservice.repositories.MailingListRepository;
import com.netcracker.dataservice.security.JwtService;
import com.netcracker.dataservice.service.converters.CsvParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final MailingListRepository mailingListRepository;
    private final CsvParser csvParser;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;
    public ResponseEntity<MailingList> createMailingList(String name,MultipartFile file, String token) {
        Set<Client> clients = csvParser.parseCsvToList(file);
        String jwt = token.substring(7);
        String customerUsername = jwtService.extractUsername(jwt);
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(customerUsername);
        if(optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            MailingList list = new MailingList(name, clients,customer);
            mailingListRepository.save(list);
            return new ResponseEntity(list,HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }
}
