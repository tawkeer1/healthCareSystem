package com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepo extends JpaRepository<User,Long> {
}
