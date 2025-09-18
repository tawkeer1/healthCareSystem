package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.AppointmentNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Medication;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Prescription;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{id}/addPrescription")
    public Prescription addPrescription(@PathVariable("id") Long doctorId, @RequestParam("appointmentId") Long appointmentId, @Valid @RequestBody Medication medication)
    throws DoctorNotFoundException, AppointmentNotFoundException{
        return doctorService.addPresciption(doctorId, appointmentId, medication);
    }
}
