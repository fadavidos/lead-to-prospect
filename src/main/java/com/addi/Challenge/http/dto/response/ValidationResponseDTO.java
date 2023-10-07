package com.addi.Challenge.http.dto.response;

import javax.swing.text.html.Option;
import java.util.Optional;

public class ValidationResponseDTO {
    private boolean valid;
    private String reason;

    public ValidationResponseDTO() {
        this.valid = false;
    }

    public ValidationResponseDTO(boolean valid) {
        this.valid = valid;
    }

    public ValidationResponseDTO(boolean valid, String reason) {
        this.valid = valid;
        this.reason = reason;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}

