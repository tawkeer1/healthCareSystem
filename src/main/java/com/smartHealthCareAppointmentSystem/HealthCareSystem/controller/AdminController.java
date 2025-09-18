package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.PatientNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Patient;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Response;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.User;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final DoctorService doctorService;
    private final PatientService patientService;
    @Autowired
    public AdminController(DoctorService doctorService, PatientService patientService){
        this.doctorService = doctorService;
        this.patientService = patientService;
    }
    @PostMapping("/createDoctor")
    public Doctor createDoctor(@Valid @RequestBody Doctor doctor){
       return doctorService.createDoctor(doctor);
    }

    @PatchMapping("/updateDoctor/{id}")
    public Doctor updateDoctor(@PathVariable("id") Long id, @Valid @RequestBody Doctor updatedDoctor) throws DoctorNotFoundException{
        return doctorService.updateDoctor(id,updatedDoctor);
    }
    @DeleteMapping("/deleteDoctor/{id}")
    public String deleteDoctor(@PathVariable("id") Long id) throws DoctorNotFoundException{
        return doctorService.deleteDoctor(id);
    }
    @PostMapping("/createPatient")
    public ResponseEntity<Response> createPatient(@Valid @RequestBody Patient patient) throws PatientNotFoundException{
         return patientService.createPatient(patient);
    }
    @PatchMapping("/updatePatient/{id}")
    public Patient updatePatient(@PathVariable("id") Long id, @Valid @RequestBody Patient patient) throws PatientNotFoundException{
        return patientService.updatePatient(id,patient);
    }
    @DeleteMapping("/deletePatient/{id}")
    public String deletePatient(@PathVariable("id") Long id) throws PatientNotFoundException{
        return patientService.deletePatient(id);
    }
}
