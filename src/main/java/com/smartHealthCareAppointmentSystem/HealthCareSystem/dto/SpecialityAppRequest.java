package com.smartHealthCareAppointmentSystem.HealthCareSystem.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SpecialityAppRequest {
    @NotNull
    String speciality;
    @NotNull
    LocalDateTime startTime;
    @NotNull
    LocalDateTime endTime;

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
