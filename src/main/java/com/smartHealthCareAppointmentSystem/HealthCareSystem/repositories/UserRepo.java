package com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User,Long> {
}
