package com.netcracker.dataservice.repositories;

import com.netcracker.dataservice.model.Client;
import com.netcracker.dataservice.model.MailingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Set;
import java.util.UUID;

@Repository
public interface MailingListRepository extends JpaRepository<MailingList, UUID> {

    @Query("select ml.clients from MailingList ml where ml.id=:id")
    Set<Client> getAllClientsByMailingListId(UUID id);

    Set<MailingList> findAllByCustomerId(UUID id);
    Set<MailingList> findAllByCustomerUsername(String id);

    boolean existsByCustomerIdAndName(UUID id, String name);

}
