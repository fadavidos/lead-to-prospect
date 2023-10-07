package com.addi.Challenge.services;

import com.addi.Challenge.repositories.LeadRepository;
import com.addi.Challenge.entities.Lead;
import com.addi.Challenge.entities.Prospect;
import com.addi.Challenge.http.dto.response.JudicialRecordDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
public class LeadService {

    private final NationalRegistryService nationalRegistryService;
    private final JudicialService judicialService;
    private final ProspectQualificationService prospectQualificationService;
    private final LeadRepository leadRepository;
    private final ProspectService prospectService;

    @Autowired
    public LeadService(NationalRegistryService nationalRegistryService, JudicialService judicialService,
                       ProspectQualificationService prospectQualificationService, LeadRepository leadRepository,
                       ProspectService prospectService) {
        this.nationalRegistryService = nationalRegistryService;
        this.judicialService = judicialService;
        this.prospectQualificationService = prospectQualificationService;
        this.leadRepository = leadRepository;
        this.prospectService = prospectService;
    }

    private CompletableFuture<Boolean> validateInfoWithNationalRegistry(Lead lead) {
        return nationalRegistryService.getPersonalInformation(lead)
                .thenApply(responseRegistry -> {
                    if (responseRegistry.isPresent()) {
                        if (lead.getNationalIdNumber().equals(responseRegistry.get().getNationalIdNumber())
                                && lead.getFirstName().equals(responseRegistry.get().getFirstName())
                                && lead.getLastName().equals(responseRegistry.get().getLastName())
                                && lead.getEmail().equals(responseRegistry.get().getEmail())) {
                            return true;
                        } else {
                            throw new RuntimeException("Data do not match");
                        }
                    } else {
                        throw new RuntimeException("The person was not found in the national registry");
                    }
                });
    }

    public CompletableFuture<Optional<Prospect>> convertLeadToProspect(String nationalIdNumber) throws Exception {
        if(prospectService.existProspectByIdentification(nationalIdNumber)) {
            throw new Exception("The lead is already a prospect");
        }
        Optional<Lead> leadBD = leadRepository
                .findByNationalIdNumber(nationalIdNumber);
        Lead lead;
        if(leadBD.isPresent()) {
            lead = leadBD.get();
        } else {
            throw new Exception("The lead does not exist in the database");
        }
        CompletableFuture<Boolean> callServiceNationalRegistry = validateInfoWithNationalRegistry(lead);
        CompletableFuture<List<JudicialRecordDTO>> callJudicialService = judicialService.getJudicialRecords(lead);

        return callServiceNationalRegistry.thenCombineAsync(callJudicialService, (responseNationalService, responseJudicialService) -> {

            if(!responseJudicialService.isEmpty()) {
                throw new RuntimeException("The person has judicial records");
            }
            if(prospectQualificationService.getPersonalQualification(responseJudicialService, lead) > 60) {
                Prospect prospect = new Prospect(
                        lead.getNationalIdNumber(),
                        lead.getBirthdate(),
                        lead.getFirstName(),
                        lead.getLastName(),
                        lead.getEmail()
                );
                prospectService.saveProspect(prospect);
                return Optional.of(prospect);
            } else {
                throw new RuntimeException("The score is less or equals to 60");
            }
        });
    }
}
