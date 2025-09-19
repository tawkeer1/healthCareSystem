package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeneralController {
    @RequestMapping("/")
    public String postLogin(@RequestParam("continue") String continueUrl){
        return "Welcome";
    }
    @GetMapping("/api/patient")
    public String patient(){
        return "Patient";
    }
    @GetMapping("/api/doctor")
    public String doctor(){
        return "Doctor";
    }
    @GetMapping("/api/admin")
    public String admin(){
        return "Admin";
    }
}
