package com.netcracker.dataservice.repositories;

import com.netcracker.dataservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    @Query("select mc.clients from SendingOrder mc where mc.id=:uuid")
    List<Client> getAllByOrderId(UUID uuid);
}
