package com.addi.Challenge.services;

import com.addi.Challenge.entities.Lead;
import com.addi.Challenge.http.dto.response.JudicialRecordDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static com.addi.Challenge.DataTest.*;

@Service
public class JudicialService {

    @Async
    public CompletableFuture<List<JudicialRecordDTO>> getJudicialRecords(Lead lead){
        return CompletableFuture.supplyAsync(() -> {

            try {
                Thread.sleep(new Random().nextLong(500, 3000));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if( lead.getNationalIdNumber().equals(LEAD_SUCCESS) ||
                    lead.getNationalIdNumber().equals(LEAD_NO_NATIONAL_REGISTRY)  ||
                    lead.getNationalIdNumber().equals(LEAD_SCORE_50)  ||
                    lead.getNationalIdNumber().equals(LEAD_NATIONAL_REGISTRY_DONT_MATCH)
            ) {
                return List.of();
            } else if (lead.getNationalIdNumber().equals(LEAD_RECORDS_JUDICIAL)) {
                return List.of(
                        new JudicialRecordDTO(lead.getNationalIdNumber(), "murder"),
                        new JudicialRecordDTO(lead.getNationalIdNumber(), "robbery")
                );
            } else {
                return List.of(
                        new JudicialRecordDTO(lead.getNationalIdNumber(), "murder"),
                        new JudicialRecordDTO(lead.getNationalIdNumber(), "robbery")
                );
            }
        });
    }
}
