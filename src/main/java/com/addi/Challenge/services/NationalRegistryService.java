package com.addi.Challenge.services;

import com.addi.Challenge.entities.Lead;
import com.addi.Challenge.http.dto.response.RegistryPersonalInfoDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import static com.addi.Challenge.DataTest.*;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class NationalRegistryService {

    @Async
    public CompletableFuture<Optional<RegistryPersonalInfoDTO>> getPersonalInformation(Lead lead) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(new Random().nextLong(500, 3000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String firstName;
            if (lead.getNationalIdNumber().equals(LEAD_SUCCESS) ||
                lead.getNationalIdNumber().equals(LEAD_SCORE_50) ||
                lead.getNationalIdNumber().equals(LEAD_RECORDS_JUDICIAL)) {
                Optional<RegistryPersonalInfoDTO> dto = Optional.of(new RegistryPersonalInfoDTO(
                        lead.getNationalIdNumber(),
                        lead.getBirthdate(),
                        lead.getFirstName(),
                        lead.getLastName(),
                        lead.getEmail()
                ));
                return dto;
            } else if (lead.getNationalIdNumber().equals(LEAD_NO_NATIONAL_REGISTRY)) {
                return Optional.empty();
            } else if( lead.getNationalIdNumber().equals(LEAD_NATIONAL_REGISTRY_DONT_MATCH)) {
                firstName = "error";
            } else {
                firstName = "error";
            }
            Optional<RegistryPersonalInfoDTO> dto = Optional.of(new RegistryPersonalInfoDTO(
                    lead.getNationalIdNumber(),
                    lead.getBirthdate(),
                    firstName,
                    lead.getLastName(),
                    lead.getEmail()
            ));
            return dto;
        });
    }
}
