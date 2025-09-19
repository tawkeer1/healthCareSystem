package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.PatientNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.UserNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AdminService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AdminService adminService;
    @Autowired
    public AdminController(DoctorService doctorService, PatientService patientService, AdminService adminService){
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.adminService = adminService;
    }
    @PostMapping("/createDoctor")
    public Doctor createDoctor(@Valid @RequestBody Doctor doctor) throws DoctorNotFoundException{
       return doctorService.createDoctor(doctor);
    }

    @PostMapping("/createDoctorForUser")
    public Doctor createDoctorForUser(@RequestParam("userId") Long userId, @Valid @RequestBody DoctorRequest doctor) throws UserNotFoundException,DoctorNotFoundException {
        return doctorService.createDoctorIfUserExists(doctor,userId);
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
    public Patient createPatient(@Valid @RequestBody Patient patient) throws PatientNotFoundException{
         return patientService.createPatient(patient);
    }

    @PostMapping("/createPatientForUser")
    public Patient createPaitentForUser(@RequestParam("userId") Long userId, @Valid @RequestBody PatientRequest patient) throws UserNotFoundException{
        return patientService.createPatientIfUserExists(patient, userId);
    }
    @PatchMapping("/updatePatient/{id}")
    public Patient updatePatient(@PathVariable("id") Long id, @Valid @RequestBody Patient patient) throws PatientNotFoundException{
        return patientService.updatePatient(id,patient);
    }
    @DeleteMapping("/deletePatient/{id}")
    public String deletePatient(@PathVariable("id") Long id) throws PatientNotFoundException{
        return patientService.deletePatient(id);
    }
    @GetMapping("/allPatients/{pageNum}")
    public List<Patient> getAllPatients(@PathVariable("pageNum") int page){
        return adminService.getAllPatients(page);
    }
    @GetMapping("/allDoctors")
    public List<Doctor> getAllDoctors(){
        return adminService.getAllDoctors();
    }
}
