package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.SendStatusPerClient;
import com.netcracker.dataservice.repositories.SendStatusPerClientRepository;
import dto.SendStatus;
import dto.StatusPerClientUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatusPerClientUpdateService {


    private final SendStatusPerClientRepository repository;

    public void updateEmailStatuses(StatusPerClientUpdateRequest request) {
        List<UUID> clientUUIDIds = request.getClientsID().stream().map(UUID::fromString).collect(Collectors.toList());
       List<SendStatusPerClient> statusPerClientList =
               repository.findAllByOrderIdAndClientIdIsIn(UUID.fromString(request.getOrderId()),clientUUIDIds);

       statusPerClientList.forEach(s->s.setEmailStatusPerClient(SendStatus.SENT));
       repository.saveAll(statusPerClientList);
    }


    public void updateSmsStatuses(StatusPerClientUpdateRequest request) {
        List<UUID> clientUUIDIds = request.getClientsID().stream().map(UUID::fromString).collect(Collectors.toList());
        List<SendStatusPerClient> statusPerClientList =
                repository.findAllByOrderIdAndClientIdIsIn(UUID.fromString(request.getOrderId()),clientUUIDIds);

        statusPerClientList.forEach(s->s.setSmsStatusPerClient(SendStatus.SENT));
        repository.saveAll(statusPerClientList);
    }


    public ResponseEntity<List<SendStatusPerClient>> getStatusesByOrderId(String id) {
       return new ResponseEntity<>(repository.findAllByOrderId(UUID.fromString(id)), HttpStatus.OK);
    }
}
