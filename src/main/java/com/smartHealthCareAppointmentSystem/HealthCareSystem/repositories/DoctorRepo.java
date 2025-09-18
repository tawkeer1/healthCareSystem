package com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.print.Doc;
import java.util.List;


public interface DoctorRepo extends JpaRepository<Doctor,Long> {
    Doctor findDoctorById(Long id);
    Doctor findDoctorBySpeciality(String speciality);
    List<Doctor> findDoctorsBySpeciality(String speciality);
}
