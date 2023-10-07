package com.addi.Challenge.repositories;

import com.addi.Challenge.entities.Lead;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LeadRepository extends JpaRepository<Lead, Long> {

    Optional<Lead> findByNationalIdNumber(String name);
}
