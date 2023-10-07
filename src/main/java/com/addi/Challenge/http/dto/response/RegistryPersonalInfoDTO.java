package com.addi.Challenge.http.dto.response;

public class RegistryPersonalInfoDTO {

    private String nationalIdNumber;
    private Long birthdate;
    private String firstName;
    private String lastName;
    private String email;

    public RegistryPersonalInfoDTO(String nationalIdNumber, Long birthdate, String firstName, String lastName, String email) {
        this.nationalIdNumber = nationalIdNumber;
        this.birthdate = birthdate;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getNationalIdNumber() {
        return nationalIdNumber;
    }

    public void setNationalIdNumber(String nationalIdNumber) {
        this.nationalIdNumber = nationalIdNumber;
    }

    public Long getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Long birthdate) {
        this.birthdate = birthdate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
