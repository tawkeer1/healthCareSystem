package com.smartHealthCareAppointmentSystem.HealthCareSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Doctor extends User{
    @Size(min = 2, max = 50, message = "Speciality should be between 2 and 50 characters")
    @NotNull
    private String speciality;
    @Size(min = 3, max = 20, message = "License number should be between 3 and 20 characters")
    @NotNull
    private String licenseNumber;
    @NotNull
    Long appCount = 0L;

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Long getAppCount() {
        return appCount;
    }

    public void setAppCount(Long appCount) {
        this.appCount = appCount;
    }
}
