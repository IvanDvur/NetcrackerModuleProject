package com.netcracker.dataservice.repositories;

import com.netcracker.dataservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {
    Client findClientById(UUID id);

    @Modifying
    @Query(value = "insert into Client (id,first_name,last_name,email,phone_number,properties, mailing_list_id) VALUES (:id,:first_name,:last_name,:email,:phone_number,:properties,:mailing_list_id)", nativeQuery = true)
    @Transactional
    void saveNewClient(@Param("id") UUID id,@Param("first_name") String first_name, @Param("last_name") String last_name,
                       @Param("email") String email, @Param("phone_number") String phone_number,
                       @Param("properties") String properties, @Param("mailing_list_id") UUID mailing_list_id);

}
