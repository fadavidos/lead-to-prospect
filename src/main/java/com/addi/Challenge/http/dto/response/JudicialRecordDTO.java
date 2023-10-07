package com.addi.Challenge.http.dto.response;

public class JudicialRecordDTO {

    private String nationalIdNumber;
    private String judicialDescription;

    public JudicialRecordDTO(String nationalIdNumber, String judicialDescription) {
        this.nationalIdNumber = nationalIdNumber;
        this.judicialDescription = judicialDescription;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public void setNationalIdNumber(String nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }

    public String getJudicialDescription() {
        return judicialDescription;
    }

    public void setJudicialDescription(String judicialDescription) {
        this.judicialDescription = judicialDescription;
    }
}
