package com.smartHealthCareAppointmentSystem.HealthCareSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Patient extends User{
    @Size(min = 3, max = 50, message = "Please enter a address between 3 and 50 characters long")
    @NotNull
    private String address;
    @Size(min = 3, max = 50, message = "Please enter a medical history between 3 and 50 characters long")
    @NotNull
    private String medicalHistory;
    @Size(min = 3, max = 50, message = "Please enter a family medical history between 3 and 50 characters long")
    @NotNull
    private String familyHistory;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }
}
