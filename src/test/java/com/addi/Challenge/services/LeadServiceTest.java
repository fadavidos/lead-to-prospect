package com.addi.Challenge.services;


import com.addi.Challenge.repositories.LeadRepository;
import com.addi.Challenge.entities.Lead;
import com.addi.Challenge.entities.Prospect;
import com.addi.Challenge.http.dto.response.JudicialRecordDTO;
import com.addi.Challenge.http.dto.response.RegistryPersonalInfoDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class LeadServiceTest {

    @MockBean
    private NationalRegistryService nationalRegistryService;
    @MockBean
    private JudicialService judicialService;
    @MockBean
    private ProspectService prospectService;
    @MockBean
    private ProspectQualificationService prospectQualificationService;
    @MockBean
    private LeadRepository leadRepository;
    @Autowired
    private LeadService leadService;

    @Test
    public void test_success() throws Exception {
        Lead lead = new Lead("1234567890", 1696381092L, "John", "Doe", "johndoe@example.com");

        Optional<RegistryPersonalInfoDTO> responseProspect = Optional.of(new RegistryPersonalInfoDTO(
                lead.getNationalIdNumber(),
                lead.getBirthdate(),
                lead.getFirstName(),
                lead.getLastName(),
                lead.getEmail()
        ));

        when(prospectService.existProspectByIdentification(any())).thenReturn(false);
        when(nationalRegistryService.getPersonalInformation(any())).thenReturn(CompletableFuture.completedFuture(responseProspect));
        when(leadRepository.findByNationalIdNumber(any())).thenReturn(Optional.of(lead));
        when(judicialService.getJudicialRecords(any())).thenReturn(CompletableFuture.completedFuture(List.of()));
        when(prospectQualificationService.getPersonalQualification(any(), any())).thenReturn(90);
        Optional<Prospect> result = leadService.convertLeadToProspect(lead.getNationalIdNumber()).get();
        assertTrue(result.isPresent());
        assertEquals(result.map(Prospect::getNationalIdNumber).orElse("error"), lead.getNationalIdNumber());

    }

    @Test
    public void test_fail_national_registry_wrong_name() throws Exception {
        Lead lead = new Lead("1234567890", 1696381092L, "John", "Doe", "johndoe@example.com");

        Optional<RegistryPersonalInfoDTO> responseProspect = Optional.of(new RegistryPersonalInfoDTO(
                lead.getNationalIdNumber(),
                lead.getBirthdate(),
                "wrongName",
                lead.getLastName(),
                lead.getEmail()
        ));

        when(prospectService.existProspectByIdentification(any())).thenReturn(false);
        when(nationalRegistryService.getPersonalInformation(any())).thenReturn(CompletableFuture.completedFuture(responseProspect));
        when(leadRepository.findByNationalIdNumber(any())).thenReturn(Optional.of(lead));
        when(judicialService.getJudicialRecords(any())).thenReturn(CompletableFuture.completedFuture(List.of()));
        ExecutionException executionException = assertThrows(ExecutionException.class,
                () -> leadService.convertLeadToProspect(lead.getNationalIdNumber()).get());
        Throwable cause = executionException.getCause();

        if (cause instanceof RuntimeException) {
            assertEquals("Data do not match", cause.getMessage());
        } else {
            fail("Expected RuntimeException, but got: " + cause.getClass().getSimpleName());
        }
    }

    @Test
    public void test_fail_national_registry_not_found() throws Exception {
        Lead lead = new Lead("1234567890", 1696381092L, "John", "Doe", "johndoe@example.com");

        when(prospectService.existProspectByIdentification(any())).thenReturn(false);
        when(nationalRegistryService.getPersonalInformation(any())).thenReturn(CompletableFuture.completedFuture(Optional.empty()));
        when(leadRepository.findByNationalIdNumber(any())).thenReturn(Optional.of(lead));
        when(judicialService.getJudicialRecords(any())).thenReturn(CompletableFuture.completedFuture(List.of()));
        ExecutionException executionException = assertThrows(ExecutionException.class,
                () -> leadService.convertLeadToProspect(lead.getNationalIdNumber()).get());
        Throwable cause = executionException.getCause();

        if (cause instanceof RuntimeException) {
            assertEquals("The person was not found in the national registry", cause.getMessage());
        } else {
            fail("Expected RuntimeException, but got: " + cause.getClass().getSimpleName());
        }
    }

    @Test
    public void test_fail_qualification_10() throws Exception {
        Lead lead = new Lead("1234567890", 1696381092L, "John", "Doe", "johndoe@example.com");

        Optional<RegistryPersonalInfoDTO> responseProspect = Optional.of(new RegistryPersonalInfoDTO(
                lead.getNationalIdNumber(),
                lead.getBirthdate(),
                lead.getFirstName(),
                lead.getLastName(),
                lead.getEmail()
        ));

        when(prospectService.existProspectByIdentification(any())).thenReturn(false);
        when(nationalRegistryService.getPersonalInformation(any())).thenReturn(CompletableFuture.completedFuture(responseProspect));
        when(leadRepository.findByNationalIdNumber(any())).thenReturn(Optional.of(lead));
        when(judicialService.getJudicialRecords(any())).thenReturn(CompletableFuture.completedFuture(List.of()));
        when(prospectQualificationService.getPersonalQualification(any(), any())).thenReturn(20);
        ExecutionException executionException = assertThrows(ExecutionException.class,
                () -> leadService.convertLeadToProspect(lead.getNationalIdNumber()).get());
        Throwable cause = executionException.getCause();

        if (cause instanceof RuntimeException) {
            assertEquals("The score is less or equals to 60", cause.getMessage());
        } else {
            fail("Expected RuntimeException, but got: " + cause.getClass().getSimpleName());
        }
    }

    @Test
    public void test_fail_lead_has_judicial_records() throws Exception {
        Lead lead = new Lead("1234567890", 1696381092L, "John", "Doe", "johndoe@example.com");

        Optional<RegistryPersonalInfoDTO> responseProspect = Optional.of(new RegistryPersonalInfoDTO(
                lead.getNationalIdNumber(),
                lead.getBirthdate(),
                lead.getFirstName(),
                lead.getLastName(),
                lead.getEmail()
        ));

        when(prospectService.existProspectByIdentification(any())).thenReturn(false);
        when(nationalRegistryService.getPersonalInformation(any())).thenReturn(CompletableFuture.completedFuture(responseProspect));
        when(leadRepository.findByNationalIdNumber(any())).thenReturn(Optional.of(lead));
        when(judicialService.getJudicialRecords(any())).thenReturn(CompletableFuture.completedFuture(List.of(new JudicialRecordDTO(lead.getNationalIdNumber(), "murder"))));
        ExecutionException executionException = assertThrows(ExecutionException.class, () ->
                leadService.convertLeadToProspect(lead.getNationalIdNumber()).get());
        Throwable cause = executionException.getCause();

        if (cause instanceof RuntimeException) {
            assertEquals("The person has judicial records", cause.getMessage());
        } else {
            fail("Expected RuntimeException, but got: " + cause.getClass().getSimpleName());
        }
    }

    @Test
    public void test_fail_lead_repository_not_found() throws Exception {
        Lead lead = new Lead("1234567890", 1696381092L, "John", "Doe", "johndoe@example.com");

        Optional<RegistryPersonalInfoDTO> responseProspect = Optional.of(new RegistryPersonalInfoDTO(
                lead.getNationalIdNumber(),
                lead.getBirthdate(),
                lead.getFirstName(),
                lead.getLastName(),
                lead.getEmail()
        ));

        when(prospectService.existProspectByIdentification(any())).thenReturn(false);
        when(nationalRegistryService.getPersonalInformation(any())).thenReturn(CompletableFuture.completedFuture(responseProspect));
        when(leadRepository.findByNationalIdNumber(any())).thenReturn(Optional.empty());
        when(judicialService.getJudicialRecords(any())).thenReturn(CompletableFuture.completedFuture(List.of()));

        Exception exception = assertThrows(Exception.class,
                () -> leadService.convertLeadToProspect(lead.getNationalIdNumber()).get());
        assertEquals("The lead does not exist in the database", exception.getMessage());
    }

    @Test
    public void test_fail_lead_repository_exception() throws Exception {
        Lead lead = new Lead("1234567890", 1696381092L, "John", "Doe", "johndoe@example.com");

        Optional<RegistryPersonalInfoDTO> responseProspect = Optional.of(new RegistryPersonalInfoDTO(
                lead.getNationalIdNumber(),
                lead.getBirthdate(),
                lead.getFirstName(),
                lead.getLastName(),
                lead.getEmail()
        ));

        when(prospectService.existProspectByIdentification(any())).thenReturn(false);
        when(nationalRegistryService.getPersonalInformation(any())).thenReturn(CompletableFuture.completedFuture(responseProspect));
        when(leadRepository.findByNationalIdNumber(any())).thenThrow(new RuntimeException("Database connection"));
        when(judicialService.getJudicialRecords(any())).thenReturn(CompletableFuture.completedFuture(List.of()));
        Exception exception = assertThrows(Exception.class,
                () -> leadService.convertLeadToProspect(lead.getNationalIdNumber()).get());
        assertEquals("Database connection", exception.getMessage());
    }

    @Test
    public void test_fail_prospect_already_exist() throws Exception {
        Lead lead = new Lead("1234567890", 1696381092L, "John", "Doe", "johndoe@example.com");

        Optional<RegistryPersonalInfoDTO> responseProspect = Optional.of(new RegistryPersonalInfoDTO(
                lead.getNationalIdNumber(),
                lead.getBirthdate(),
                lead.getFirstName(),
                lead.getLastName(),
                lead.getEmail()
        ));

        when(prospectService.existProspectByIdentification(any())).thenReturn(true);
        Exception exception = assertThrows(Exception.class,
                () -> leadService.convertLeadToProspect(lead.getNationalIdNumber()).get());
        assertEquals("The lead is already a prospect", exception.getMessage());
    }
}
