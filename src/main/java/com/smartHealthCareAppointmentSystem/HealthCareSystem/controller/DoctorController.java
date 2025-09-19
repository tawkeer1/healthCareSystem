package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.AppointmentNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Appointment;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Medication;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Prescription;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AppointmentRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    DoctorService doctorService;
    @Autowired
    public DoctorController(DoctorService doctorService){
        this.doctorService = doctorService;
    }
    @PatchMapping("/{id}/markAppointmentAsCompleted")
    public String markAppointmentAsCompleted(@PathVariable("id") Long doctorId, @RequestParam("appointmentId") Long appointmentId) throws DoctorNotFoundException, AppointmentNotFoundException {
        return doctorService.markAppointmentAsCompleted(doctorId, appointmentId);
    }
//    @GetMapping("/test")
//    public String test(Authentication auth) {
//        System.out.println(auth.getAuthorities());
//        return "test";
//    }
    @PostMapping("/{id}/addPrescription")
    public Prescription addPrescription(@PathVariable("id") Long doctorId, @RequestParam("appointmentId") Long appointmentId, @Valid @RequestBody Medication medication)
    throws DoctorNotFoundException, AppointmentNotFoundException{
        return doctorService.addPrescription(doctorId, appointmentId, medication);
    }
    @GetMapping("/{id}/getTodayAppointments")
    public List<Appointment> getTodayAppointments(@PathVariable("id") Long doctorId){
        return doctorService.getTodayAppointments(doctorId);
    }
    @GetMapping("/{id}/getAppointments")
    public List<Appointment> getAllAppointments(@PathVariable("id") Long id){
        return doctorService.getAllAppointments(id);
    }
}
