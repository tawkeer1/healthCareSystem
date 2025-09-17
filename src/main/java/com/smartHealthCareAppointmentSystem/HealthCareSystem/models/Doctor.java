package com.smartHealthCareAppointmentSystem.HealthCareSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL, targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @Size(min = 2, max = 50, message = "Speciality should be between 2 and 50 characters")
    private String speciality;
    @Size(min = 3, max = 20, message = "License number should be between 3 and 20 characters")
    private String licenseNumber;
//    private LocalDateTime startTime;
//    private LocalDateTime endTime;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

//    public LocalDateTime getStartTime() {
//        return startTime;
//    }
//
//    public void setStartTime(LocalDateTime startTime) {
//        this.startTime = startTime;
//    }
//
//    public LocalDateTime getEndTime() {
//        return endTime;
//    }
//
//    public void setEndTime(LocalDateTime endTime) {
//        this.endTime = endTime;
//    }
}
