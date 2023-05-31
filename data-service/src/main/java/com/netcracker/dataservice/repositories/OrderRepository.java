package com.netcracker.dataservice.repositories;

import com.netcracker.dataservice.model.SendingOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
@Repository
public interface OrderRepository extends JpaRepository<SendingOrder, UUID> {
    Optional<SendingOrder> findById(UUID uuid);

    List<SendingOrder> findAllByCustomerId(UUID uuid);

    Set<SendingOrder> findAllByCustomerUsername(String username);
    boolean existsByMailingListId(UUID uuid);
}
