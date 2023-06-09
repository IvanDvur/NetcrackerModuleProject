package com.netcracker.dataservice.repositories;

import com.netcracker.dataservice.model.SendStatusPerClient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SendStatusPerClientRepository extends JpaRepository<SendStatusPerClient, UUID> {

}
