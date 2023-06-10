package com.netcracker.dataservice.service;

import com.netcracker.dataservice.model.SendStatusPerClient;
import com.netcracker.dataservice.repositories.SendStatusPerClientRepository;
import dto.SendStatus;
import dto.StatusPerClientUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatusPerClientUpdateService {


    private final SendStatusPerClientRepository repository;

    public void updateStatuses(StatusPerClientUpdateRequest request) {
        List<UUID> clientUUIDIds = request.getClientsID().stream().map(UUID::fromString).collect(Collectors.toList());
       List<SendStatusPerClient> statusPerClientList =
               repository.findAllByOrderIdAndClientIdIsIn(UUID.fromString(request.getOrderId()),clientUUIDIds);

       statusPerClientList.forEach(s->s.setEmailStatusPerClient(SendStatus.SENT));
       repository.saveAll(statusPerClientList);
    }

}
