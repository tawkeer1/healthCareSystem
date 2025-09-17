package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.User;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepo userRepo;
    @Autowired
    public UserService(UserRepo userRepo){
        this.userRepo = userRepo;
    }
    public User createUser(User user){
        return userRepo.save(user);
    }
}
