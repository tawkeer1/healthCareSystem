package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.dto.DoctorRequest;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.UserContextService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    DoctorService doctorService;
    UserContextService userContextService;
    @Autowired
    public DoctorController(DoctorService doctorService, UserContextService userContextService){
        this.doctorService = doctorService;
        this.userContextService = userContextService;
    }

    @PatchMapping("/markAppointmentAsCompleted")
    public String markAppointmentAsCompleted( @RequestParam("appointmentId") Long appointmentId, Authentication authentication) throws
           DoctorNotFoundException, AppointmentNotFoundException, NotValidAppointmentException, InvalidLoginDetailsException {
        Doctor doctor = userContextService.getAuthenticatedDoctor(authentication);
        return doctorService.markAppointmentAsCompleted(doctor,appointmentId);
    }

    @PostMapping("/addPrescription")
    public Prescription addPrescription(@RequestParam("appointmentId") Long appointmentId, @Valid @RequestBody Medication medication, Authentication authentication)
            throws AppointmentNotFoundException,DoctorNotFoundException,
            InvalidLoginDetailsException,PrescriptionAlreadyAddedException,NotValidAppointmentActionException {
        Doctor doctor = userContextService.getAuthenticatedDoctor(authentication);
        return doctorService.addPrescription(appointmentId,doctor,medication);
    }
    @GetMapping("/getTodayAppointments")
    public List<Appointment> getTodayAppointments(Authentication authentication) throws  UserNotFoundException,InvalidLoginDetailsException{
        Doctor doctor = userContextService.getAuthenticatedDoctor(authentication);
        return doctorService.getTodayAppointments(doctor);
    }
    @GetMapping("/getAppointments")
    public List<Appointment> getAllAppointments(Authentication authentication) throws InvalidLoginDetailsException{
        Doctor doctor = userContextService.getAuthenticatedDoctor(authentication);
        return doctorService.getAllAppointments(doctor);
    }

    @PatchMapping("/updatePersonalDetails")
    public Doctor updatePersonalDetails(@Valid @RequestBody DoctorRequest request, Authentication authentication) throws DoctorNotFoundException,InvalidLoginDetailsException{
        Doctor doctor = userContextService.getAuthenticatedDoctor(authentication);
        return doctorService.updatePersonalDetails(request,doctor);
    }
}
