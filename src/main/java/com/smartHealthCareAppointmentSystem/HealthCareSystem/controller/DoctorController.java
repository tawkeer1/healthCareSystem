package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.AppointmentNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.UnauthorizedUserException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.UserNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AppointmentRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.UserRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {
    DoctorService doctorService;
    @Autowired
    public DoctorController(DoctorService doctorService){

        this.doctorService = doctorService;
    }
//    @PatchMapping("/{id}/markAppointmentAsCompleted")
//    public String markAppointmentAsCompleted(@PathVariable("id") Long doctorId, @RequestParam("appointmentId") Long appointmentId) throws DoctorNotFoundException, AppointmentNotFoundException {
//        return doctorService.markAppointmentAsCompleted(doctorId, appointmentId);
//    }
    @PatchMapping("/markAppointmentAsCompleted")
    public String markAppointmentAsCompleted( @RequestParam("appointmentId") Long appointmentId, Authentication authentication) throws UserNotFoundException, DoctorNotFoundException, AppointmentNotFoundException {
        return doctorService.markAppointmentAsCompleted(appointmentId,authentication);
    }
//    @GetMapping("/test")
//    public String test(Authentication auth) {
//        System.out.println(auth.getAuthorities());
//        return "test";
//    }
//    @PostMapping("/{id}/addPrescription")
//    public Prescription addPrescription(@PathVariable("id") Long doctorId, @RequestParam("appointmentId") Long appointmentId, @Valid @RequestBody Medication medication)
//    throws DoctorNotFoundException, AppointmentNotFoundException{
//        return doctorService.addPrescription(doctorId, appointmentId, medication);
//    }

    @PostMapping("/addPrescription")
    public Prescription addPrescription(@RequestParam("appointmentId") Long appointmentId, @Valid @RequestBody Medication medication, Authentication authentication)
            throws AppointmentNotFoundException,DoctorNotFoundException, UserNotFoundException {
        return doctorService.addPrescription(appointmentId,medication,authentication);
    }
//    @GetMapping("/{id}/getTodayAppointments")
//    public List<Appointment> getTodayAppointments(@PathVariable("id") Long doctorId){
//        return doctorService.getTodayAppointments(doctorId);
//    }
    @GetMapping("/getTodayAppointments")
    public List<Appointment> getTodayAppointments(Authentication authentication) throws  UserNotFoundException{
        return doctorService.getTodayAppointments(authentication);
    }
    @GetMapping("/getAppointments")
    public List<Appointment> getAllAppointments(Authentication authentication) throws UserNotFoundException{

        return doctorService.getAllAppointments(authentication);
    }
//    @PatchMapping("/{id}/updatePersonalDetails")
//    public Doctor updatePersonalDetails(@PathVariable("id") Long id, @RequestBody DoctorRequest request, Authentication authentication) throws UnauthorizedUserException {
//        return doctorService.updatePersonalDetails(id,request, authentication);
//    }
    @PatchMapping("/updatePersonalDetails")
    public Doctor updatePersonalDetails( @RequestBody DoctorRequest request, Authentication authentication) throws DoctorNotFoundException,UserNotFoundException{
        return doctorService.updatePersonalDetails(request, authentication);
    }
}
