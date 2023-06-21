package com.netcracker.dataservice.repositories;

import com.netcracker.dataservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByUsername(String username);

    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    List<Customer> findAllByLastLoginBefore(LocalDateTime time);
}
