package com.netcracker.dataservice.service;


import com.netcracker.dataservice.model.MailingList;
import com.netcracker.dataservice.repositories.MailingListRepository;
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

    public ResponseEntity<Set<MailingList>> getMailingLists(String token) {
        String jwt = token.substring(7);
        String customerUsername = jwtService.extractUsername(jwt);
        Set<MailingList> mailingLists = repository.findAllByCustomerUsername(customerUsername);
        mailingLists.forEach(x->x.setQuantityOfClients(x.getClients().size()));
        return ResponseEntity.ok(mailingLists);
    }

    public void deleteMailingList(String id, String token) {
        String jwt = token.substring(7);
        String tokenUsername = jwtService.extractUsername(jwt);
        MailingList listToDelete = repository.findById(UUID.fromString(id)).get();
        if(listToDelete.getCustomer().getUsername().equals(tokenUsername)){
            repository.delete(listToDelete);
        }

    }
}
