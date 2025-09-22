package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.UserNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.User;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping("/create")
    public User createUser(@Valid @RequestBody User user){
        return userService.createUser(user);
    }
    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) throws UserNotFoundException {
        return userService.deleteUser(id);
    }
}
