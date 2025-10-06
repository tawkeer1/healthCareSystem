package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.InvalidLoginDetailsException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Patient;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.DoctorRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserContextService {
    private final PatientRepo patientRepo;
    private final DoctorRepo doctorRepo;
    @Autowired
    public UserContextService(PatientRepo patientRepo,DoctorRepo doctorRepo) {
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
    }

    public Patient getAuthenticatedPatient(Authentication authentication) throws InvalidLoginDetailsException {
        String email = authentication.getName();
        if (email == null) throw new InvalidLoginDetailsException("Login details not found. Please login");
        Patient patient = patientRepo.findByEmail(email);
        if (patient == null) throw new InvalidLoginDetailsException("No patient found for logged-in user");
        return patient;
    }
    public Doctor getAuthenticatedDoctor(Authentication authentication) throws InvalidLoginDetailsException {
        String email = authentication.getName();
        if (email == null) throw new InvalidLoginDetailsException("Login details not found. Please login");
        Doctor doctor = doctorRepo.findByEmail(email);
        if (doctor == null) throw new InvalidLoginDetailsException("No doctor found for logged-in user");
        return doctor;
    }
}

