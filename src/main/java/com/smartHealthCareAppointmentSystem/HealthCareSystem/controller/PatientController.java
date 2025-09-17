package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.PatientNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Appointment;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Patient;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final DoctorService doctorService;
    private final PatientService patientService;
    @Autowired
    public PatientController(DoctorService doctorService, PatientService patientService){
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @GetMapping("/searchDoctor")
    public Doctor searchDoctor(@RequestParam("speciality") String speciality) throws DoctorNotFoundException {
        return doctorService.searchDoctorBySpeciality(speciality);
    }

    @PatchMapping("/{id}/updatePersonalDetails")
    public Patient updatePersonalDetails(@PathVariable("id") Long id, @Valid @RequestBody Patient patient) throws PatientNotFoundException {
        return patientService.updatePersonalDetails(id,patient);
    }
//    @PostMapping("/{id}/bookAppointment")
//    public Appointment bookAppointment(@RequestParam("speciality") String speciality, @RequestParam("id") Long id){
//        return .bookAppointment(speciality, id);
//    }
}
