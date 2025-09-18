package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Appointment;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Patient;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Prescription;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AppointmentService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/patient")
public class PatientController {
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    @Autowired
    public PatientController(DoctorService doctorService, PatientService patientService, AppointmentService appointmentService){
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
    }

    @GetMapping("/searchDoctor")
    public Doctor searchDoctor(@RequestParam("speciality") String speciality) throws DoctorNotFoundException {
        return doctorService.searchDoctorBySpeciality(speciality);
    }

    @PatchMapping("/{id}/updatePersonalDetails")
    public Patient updatePersonalDetails(@PathVariable("id") Long id, @Valid @RequestBody Patient patient) throws PatientNotFoundException {
        return patientService.updatePersonalDetails(id,patient);
    }
    @PostMapping("/{id}/bookAppointment")
    public Appointment bookAppointment(@RequestParam("speciality") String speciality, @PathVariable("id") Long id) throws PatientNotFoundException, DoctorNotFoundException, DoctorBusyException {
        List<Doctor> doctors = doctorService.searchDoctorsBySpeciality(speciality);
        if(doctors.isEmpty()) throw new DoctorNotFoundException("No doctor with the speciality exists");
        // random or patients can select doctor
        int size = doctors.size();
        Doctor doctor = doctors.get(new Random().nextInt(0,size));
        return appointmentService.bookAppointment(doctor, id);
    }
    @PostMapping("/{id}/cancelAppointment")
    public String cancelAppointment(@PathVariable("id") Long patientId,
                                    @RequestParam("appointmentId") Long appointmentId) throws AppointmentNotFoundException {
        return appointmentService.cancelAppointment(patientId, appointmentId);
    }
    @GetMapping("/{id}/prescriptionHistory")
    public List<Prescription> getPrescriptionHistory(@PathVariable("id") Long id) throws PatientNotFoundException{
        return patientService.getPrescriptionHistory(id);
    }
}
