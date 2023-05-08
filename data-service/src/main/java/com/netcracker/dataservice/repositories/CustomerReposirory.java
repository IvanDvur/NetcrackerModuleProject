package com.netcracker.dataservice.repositories;

import com.netcracker.dataservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerReposirory extends JpaRepository<Customer, UUID> {


}
