package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.User;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AdminRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepo adminRepo;
    @Autowired
    public AdminService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

}
