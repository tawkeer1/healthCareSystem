package com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DoctorRepo extends JpaRepository<Doctor,Long> {
    Doctor findDoctorById(Long id);
    Doctor findDoctorBySpeciality(String speciality);
}
