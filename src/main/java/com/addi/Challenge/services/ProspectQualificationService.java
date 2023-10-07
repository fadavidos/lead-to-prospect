package com.addi.Challenge.services;

import com.addi.Challenge.entities.Lead;
import com.addi.Challenge.http.dto.response.JudicialRecordDTO;
import com.addi.Challenge.http.dto.response.RegistryPersonalInfoDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import static com.addi.Challenge.DataTest.LEAD_SUCCESS;

@Service
public class ProspectQualificationService {

    @Async
    public Integer getPersonalQualification(List<JudicialRecordDTO> judicialRecords, Lead lead) {
        if(lead.getNationalIdNumber().equals(LEAD_SUCCESS)) {
            return 90;
        }
        int value = new Random().nextInt(100);
        return value;
    }
}
