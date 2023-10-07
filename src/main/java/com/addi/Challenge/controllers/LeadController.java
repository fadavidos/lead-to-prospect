package com.addi.Challenge.controllers;

import com.addi.Challenge.http.dto.response.ValidationResponseDTO;
import com.addi.Challenge.entities.Prospect;
import com.addi.Challenge.http.dto.request.LeadToProspectDTO;
import com.addi.Challenge.services.LeadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping("/lead-to-prospect")
    public ResponseEntity<ValidationResponseDTO> validateLeadToProspect(@RequestBody LeadToProspectDTO lead) {
        ValidationResponseDTO response = new ValidationResponseDTO();

        Optional<Prospect> optionalProspect = Optional.empty();
        try {
            optionalProspect = leadService.convertLeadToProspect(lead.getNationalIdNumber()).join();
            if (optionalProspect.isPresent()) {
                response.setValid(true);
                return ResponseEntity.ok(response);
            } else {
                response.setValid(false);
                response.setReason("Unexpected error");
                return ResponseEntity.ok().body(response);
            }
        } catch (Exception e) {
            return responseWithException(e, response);
        }
    }

    private ResponseEntity<ValidationResponseDTO> responseWithException(Exception e, ValidationResponseDTO response) {
        response.setValid(false);
        if(e.getCause() != null) {
            response.setReason(e.getCause().getMessage());
        } else {
            response.setReason(e.getMessage());
        }
        return ResponseEntity.badRequest().body(response);
    }

}
