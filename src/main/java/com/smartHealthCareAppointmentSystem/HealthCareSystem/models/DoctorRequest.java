package com.smartHealthCareAppointmentSystem.HealthCareSystem.models;

import jakarta.validation.constraints.NotNull;

public class DoctorRequest {
    String speciality;
    String licenseNumber;
    String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
