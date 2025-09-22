package com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Appointment;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorIdAndStatus(Long doctorId, Status status);
    @Query("select a from Appointment a where a.id = :id and a.status = :status and a.time = :time")
    Appointment findAppointmentByDoctorIdandStatusandTime(Long id, Status status, LocalDateTime time);
//    List<Appointment> findByDoctorIdAndAppointmentTimeBetweenAndStatus(Long id, LocalDateTime startOfDay, LocalDateTime endOfDay, Status status);
    List<Appointment> findByDoctorId(Long id);
//    @Query("select a from Appointment a where a.doctor.id = :id and a.status = :status and a.time between :startOfDay and :endOfDay")
    List<Appointment> findByDoctorIdAndStatusAndTimeBetween(Long id, Status status, LocalDateTime startOfDay, LocalDateTime endOfDay);
}

