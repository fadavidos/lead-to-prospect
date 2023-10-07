package com.addi.Challenge.http.dto.request;

public class LeadToProspectDTO {
    private String nationalIdNumber;

    public LeadToProspectDTO() {
    }

    public LeadToProspectDTO(String nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public void setNationalIdNumber(String nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }
}
