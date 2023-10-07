package com.addi.Challenge.repositories;

import com.addi.Challenge.entities.Prospect;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProspectRepository extends JpaRepository<Prospect, Long> {
    Optional<Prospect> findByNationalIdNumber(String nationalIdNumber);
}
