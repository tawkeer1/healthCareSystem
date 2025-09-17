package com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository<Patient,Long> {
    Patient findPatientById(Long id);
}
