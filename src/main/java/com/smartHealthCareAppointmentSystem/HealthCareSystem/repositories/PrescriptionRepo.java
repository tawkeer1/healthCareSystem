package com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Prescription;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PrescriptionRepo extends MongoRepository<Prescription, String> {
    List<Prescription> findByPatientId(Long id);
    List<Prescription> findByAppointmentId(Long id);
}
