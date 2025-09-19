package com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Appointment;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    Appointment findAppointmentByDoctorIdAndStatus(Long doctorId, Status status);
    @Query("select a from Appointment a where id = :id and status = :status and time = :time")
    Appointment findAppointmentByDoctorIdandStatusandTime(Long id, Status status, LocalDateTime time);
//    List<Appointment> findByDoctorIdAndAppointmentTimeBetweenAndStatus(Long id, LocalDateTime startOfDay, LocalDateTime endOfDay, Status status);
    List<Appointment> findByDoctorId(Long id);
}

