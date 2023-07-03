package com.netcracker.dataservice.service;


import com.netcracker.dataservice.dto.MailingListDto;
import com.netcracker.dataservice.model.Client;
import com.netcracker.dataservice.model.MailingList;
import com.netcracker.dataservice.repositories.ClientRepository;
import com.netcracker.dataservice.repositories.MailingListRepository;
import com.netcracker.dataservice.repositories.OrderRepository;
import com.netcracker.dataservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MailingListService {

    private final JwtService jwtService;
    private final MailingListRepository mailingListRepository;
    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;

    public ResponseEntity<Set<MailingList>> getMailingLists(String token) {
        String jwt = token.substring(7);
        String customerUsername = jwtService.extractUsername(jwt);
        Set<MailingList> mailingLists = mailingListRepository.findAllByCustomerUsername(customerUsername);
        mailingLists.forEach(x -> x.setQuantityOfClients(x.getClients().size()));
        return ResponseEntity.ok(mailingLists);
    }

    public void deleteMailingList(String id, String token) {
        String jwt = token.substring(7);
        String tokenUsername = jwtService.extractUsername(jwt);
        MailingList listToDelete = mailingListRepository.findById(UUID.fromString(id)).get();
        if (listToDelete.getCustomer().getUsername().equals(tokenUsername)) {
            if (orderRepository.existsByMailingListId(UUID.fromString(id))) {
                listToDelete.setCustomer(null);
                mailingListRepository.save(listToDelete);
            } else {
                listToDelete.setCustomer(null);
                mailingListRepository.delete(listToDelete);
            }
        }
    }

    public ResponseEntity<MailingListDto> getMailingListById(String id) {
        MailingList mailingList = mailingListRepository.findById(UUID.fromString(id)).get();
        return ResponseEntity.ok(MailingListDto.convertMailingListToDto(mailingList));
    }

    public ResponseEntity<Boolean> updateClient(Client clientToUpdate) {
        Client clientFromDb = clientRepository.findClientById(clientToUpdate.getId());
        clientFromDb.setFirstName(clientToUpdate.getFirstName());
        clientFromDb.setLastName(clientToUpdate.getLastName());
        clientFromDb.setEmail(clientToUpdate.getEmail());
        clientFromDb.setPhoneNumber(clientToUpdate.getPhoneNumber());
        clientFromDb.setProperties(clientToUpdate.getProperties());
        clientRepository.save(clientFromDb);
        return ResponseEntity.ok(true);
    }

    public void deleteClient(String id) {
        clientRepository.deleteById(UUID.fromString(id));
    }

    public ResponseEntity<Boolean> saveClient(Client client, String mailingListId) {
        if (mailingListRepository.existsById(UUID.fromString(mailingListId))) {
            clientRepository.saveNewClient(UUID.randomUUID(),
                    client.getFirstName(),
                    client.getLastName(),
                    client.getEmail(),
                    client.getPhoneNumber(),
                    client.getProperties(), UUID.fromString(mailingListId));
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
