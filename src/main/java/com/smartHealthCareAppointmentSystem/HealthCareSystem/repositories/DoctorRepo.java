package com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;

import javax.print.Doc;
import java.util.List;


public interface DoctorRepo extends JpaRepository<Doctor,Long> {
    Doctor findDoctorById(Long id);
    Doctor findByEmail(String email);
    Doctor findDoctorBySpeciality(String speciality);
    List<Doctor> findDoctorsBySpeciality(String speciality);
    @Query("select d from Doctor d order by d.appCount desc")
    @QueryHints(value = @QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Doctor> findMostFrequentlyUsedDoctors(Pageable pageable);
}
