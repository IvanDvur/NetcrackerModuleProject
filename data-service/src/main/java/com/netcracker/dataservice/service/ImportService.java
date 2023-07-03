package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.Client;
import com.netcracker.dataservice.model.Customer;
import com.netcracker.dataservice.model.MailingList;
import com.netcracker.dataservice.model.TariffConfig;
import com.netcracker.dataservice.repositories.CustomerRepository;
import com.netcracker.dataservice.repositories.MailingListRepository;
import com.netcracker.dataservice.security.JwtService;
import com.netcracker.dataservice.service.converters.CsvParser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImportService {

    private final MailingListRepository mailingListRepository;
    private final CsvParser csvParser;
    private final JwtService jwtService;
    private final CustomerRepository customerRepository;

    public ResponseEntity<String> createMailingList(String name, MultipartFile file, String token) {
        Set<Client> clients = csvParser.parseCsvToList(file);
        String jwt = token.substring(7);
        String customerUsername = jwtService.extractUsername(jwt);
        Optional<Customer> optionalCustomer = customerRepository.findByUsername(customerUsername);

        if (optionalCustomer.isPresent()) {
            Customer customer = optionalCustomer.get();
            if(!checkTariff(customer)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Превышено допустимое кол-во списков по тарифу");
            }
            if(mailingListRepository.existsByCustomerIdAndName(customer.getId(),name)){
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Список с таким названием уже существует");
            }
            MailingList list = new MailingList(name, clients, customer);
            mailingListRepository.save(list);
            return new ResponseEntity("", HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    private boolean checkTariff(Customer customer) {
        int nbOfLists = mailingListRepository.findAllByCustomerId(customer.getId()).size();
        System.out.println(nbOfLists);
        switch (customer.getRole()) {
            case USER:
                return nbOfLists < TariffConfig.FREE_MAX_MAILING_LIST;
            case USER_PLUS:
                return nbOfLists < TariffConfig.PLUS_MAX_MAILING_LIST;
            case USER_PREMIUM:
                return nbOfLists < TariffConfig.PREMIUM_MAX_MAILING_LIST;
        }
        return false;
    }


}