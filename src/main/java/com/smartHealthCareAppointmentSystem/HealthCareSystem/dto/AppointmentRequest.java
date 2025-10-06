package com.smartHealthCareAppointmentSystem.HealthCareSystem.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class AppointmentRequest {
    @NotNull
    private Long doctorId;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
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
