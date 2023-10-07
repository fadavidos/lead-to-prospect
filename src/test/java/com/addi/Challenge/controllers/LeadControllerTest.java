package com.addi.Challenge.controllers;

import com.addi.Challenge.controllers.LeadController;
import com.addi.Challenge.entities.Lead;
import com.addi.Challenge.entities.Prospect;
import com.addi.Challenge.http.dto.response.ValidationResponseDTO;
import com.addi.Challenge.services.LeadService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeadController.class)
class LeadControllerTest {

    @MockBean
    private LeadService leadService;

    @Autowired
    private MockMvc mockMvc;


    @Test
    void testValidateToProspect_LeadValid() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();

        Lead lead = new Lead("1234567890", 1696381092L, "John", "Doe", "johndoe@example.com");

        Optional<Prospect> responseProspect = Optional.of(new Prospect(
                lead.getNationalIdNumber(),
                lead.getBirthdate(),
                lead.getFirstName(),
                lead.getLastName(),
                lead.getEmail()
        ));
        when(leadService.convertLeadToProspect(any())).thenReturn(CompletableFuture.completedFuture(responseProspect));

        MvcResult result = mockMvc.perform(post("/leads/lead-to-prospect")
                        .content(objectMapper.writeValueAsString(lead))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ValidationResponseDTO responseDTO = objectMapper.readValue(responseJson, ValidationResponseDTO.class);
        assertTrue(responseDTO.isValid());
        assertNull(responseDTO.getReason());
    }

    @Test
    void testValidateToProspect_LeadInvalid() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Lead lead = new Lead("1234567890", 1696381092L, "John", "Doe", "johndoe@example.com");
        when(leadService.convertLeadToProspect(any())).thenReturn(CompletableFuture.completedFuture(Optional.empty()));

        MvcResult result = mockMvc.perform(post("/leads/lead-to-prospect")
                        .content(objectMapper.writeValueAsString(lead))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ValidationResponseDTO responseDTO = objectMapper.readValue(responseJson, ValidationResponseDTO.class);
        System.out.println("is valid: " + responseDTO.isValid());
        assertFalse(responseDTO.isValid());
        assertEquals("Unexpected error", responseDTO.getReason());

    }

    @Test
    void testValidateToProspect_Exception() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Lead lead = new Lead("1234567890", 1696381092L, "John", "Doe", "johndoe@example.com");
        when(leadService.convertLeadToProspect(any())).thenThrow(new RuntimeException("fatal error"));

        MvcResult result = mockMvc.perform(post("/leads/lead-to-prospect")
                        .content(objectMapper.writeValueAsString(lead))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        ValidationResponseDTO responseDTO = objectMapper.readValue(responseJson, ValidationResponseDTO.class);
        assertFalse(responseDTO.isValid());
        assertEquals("fatal error", responseDTO.getReason());

    }
}
