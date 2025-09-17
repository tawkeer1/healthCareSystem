package com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Appointment;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {
    Appointment findAppointmentByDoctorIdAndStatus(Long doctorId, Status status);
}

//git init
//git add README.md
//git commit -m "first commit"
//git branch -M main
//git remote add origin https://github.com/tawkeer1/healthCareSystem.git
//git push -u origin main
