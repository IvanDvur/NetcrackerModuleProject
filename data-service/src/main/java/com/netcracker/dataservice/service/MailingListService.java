package com.netcracker.dataservice.service;


import com.netcracker.dataservice.dto.MailingLIstDto;
import com.netcracker.dataservice.model.Client;
import com.netcracker.dataservice.model.MailingList;
import com.netcracker.dataservice.repositories.ClientRepository;
import com.netcracker.dataservice.repositories.MailingListRepository;
import com.netcracker.dataservice.repositories.OrderRepository;
import com.netcracker.dataservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailingListService {

    private final JwtService jwtService;
    private final MailingListRepository repository;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    public ResponseEntity<Set<MailingList>> getMailingLists(String token) {
        String jwt = token.substring(7);
        String customerUsername = jwtService.extractUsername(jwt);
        Set<MailingList> mailingLists = repository.findAllByCustomerUsername(customerUsername);
        mailingLists.forEach(x -> x.setQuantityOfClients(x.getClients().size()));
        return ResponseEntity.ok(mailingLists);
    }

    public void deleteMailingList(String id, String token) {
        String jwt = token.substring(7);
        String tokenUsername = jwtService.extractUsername(jwt);
        MailingList listToDelete = repository.findById(UUID.fromString(id)).get();
        if (listToDelete.getCustomer().getUsername().equals(tokenUsername)) {
            if (orderRepository.existsByMailingListId(UUID.fromString(id))) {
                listToDelete.setCustomer(null);
                repository.save(listToDelete);
            } else {
                repository.delete(listToDelete);
            }
        }

    }
    public ResponseEntity<MailingLIstDto> getMailingListById(String id) {


        MailingList mailingList = repository.findById(UUID.fromString(id)).get();

        return ResponseEntity.ok(MailingLIstDto.convertMailingListToDto(mailingList));

    }

    public ResponseEntity<Boolean> updateClient(Client clientToUpdate) {
        Client clientFromDb=clientRepository.findClientById(clientToUpdate.getId());
        clientFromDb.setFirstName(clientToUpdate.getFirstName());
        clientFromDb.setLastName(clientToUpdate.getLastName());
        clientFromDb.setEmail(clientToUpdate.getEmail());
        clientFromDb.setPhoneNumber(clientToUpdate.getPhoneNumber());
        clientFromDb.setProperties(clientToUpdate.getProperties());
        clientRepository.save(clientFromDb);
        return  ResponseEntity.ok(true);
    }

    public void deleteClient(String id) {
        clientRepository.deleteById(UUID.fromString(id));
    }
}
