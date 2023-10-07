package com.addi.Challenge.services;

import com.addi.Challenge.entities.Prospect;
import com.addi.Challenge.repositories.ProspectRepository;
import org.springframework.stereotype.Service;

@Service
public class ProspectService {

    private final ProspectRepository prospectRepository;


    public ProspectService(ProspectRepository prospectRepository) {
        this.prospectRepository = prospectRepository;
    }

    public boolean existProspectByIdentification(String nationalIdNumber) {
        return prospectRepository.findByNationalIdNumber(nationalIdNumber).isPresent();
    }

    public void saveProspect(Prospect prospect) {
        prospectRepository.save(prospect);
    }
}
