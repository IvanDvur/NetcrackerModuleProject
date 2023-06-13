package com.netcracker.dataservice.repositories;

import com.netcracker.dataservice.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TemplateRepository extends JpaRepository<Template, UUID> {
}
