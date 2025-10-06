package com.smartHealthCareAppointmentSystem.HealthCareSystem.models;

import jakarta.validation.constraints.NotNull;

public class LabReport {
    @NotNull
    private String type;
    @NotNull
    private String result;
    @NotNull
    private String note;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
