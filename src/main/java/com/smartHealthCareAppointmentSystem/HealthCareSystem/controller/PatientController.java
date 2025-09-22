package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AppointmentService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
//    @GetMapping("/")
//    public String returnSomethig(){
//        return "Patient";
//    }

    @GetMapping("/searchDoctor")
    public Doctor searchDoctor(@RequestParam("speciality") String speciality, Authentication auth) throws DoctorNotFoundException {
        System.out.println(auth.getAuthorities());
        return doctorService.searchDoctorBySpeciality(speciality);
    }

//    @PatchMapping("/{id}/updatePersonalDetails")
//    public Patient updatePersonalDetails(@PathVariable("id") Long id, @RequestBody PatientRequest patient, Authentication authentication) throws PatientNotFoundException, UnauthorizedUserException {
//        return patientService.updatePersonalDetails(id,patient,authentication);
//    }
    @PatchMapping("/updatePersonalDetails")
    public Patient updatePersonalDetails(@RequestBody PatientRequest patient, Authentication authentication) throws UserNotFoundException, PatientNotFoundException, UnauthorizedUserException {
        return patientService.updatePersonalDetails(patient,authentication);
    }

//    @PostMapping("/{id}/bookAppointment")
//    public Appointment bookAppointment(@RequestParam("speciality") String speciality, @PathVariable("id") Long id) throws PatientNotFoundException, DoctorNotFoundException, DoctorBusyException {
//        List<Doctor> doctors = doctorService.searchDoctorsBySpeciality(speciality);
//        if(doctors.isEmpty()) throw new DoctorNotFoundException("No doctor with the speciality exists");
//        // select a random doctor from the list
//        int size = doctors.size();
//        Doctor doctor = doctors.get(new Random().nextInt(0,size));
//        return appointmentService.bookAppointment(doctor, id);
//    }
    @PostMapping("/bookAppointment")
    public Appointment bookAppointment(@RequestParam("speciality") String speciality,
                                       @RequestParam("startTime") LocalDateTime startTime,
                                       @RequestParam("endTime") LocalDateTime endTime,
                                       Authentication authentication) throws UserNotFoundException, PatientNotFoundException, DoctorNotFoundException, DoctorBusyException {
        List<Doctor> doctors = doctorService.searchDoctorsBySpeciality(speciality);
        if(doctors.isEmpty()) throw new DoctorNotFoundException("No doctor with the speciality exists");
        // select a random doctor from the list
        int size = doctors.size();
        Doctor doctor = doctors.get(new Random().nextInt(0,size));
        return appointmentService.bookAppointment(doctor,startTime,endTime,authentication);
    }
//    @PostMapping("/{id}/cancelAppointment")
//    public String cancelAppointment(@PathVariable("id") Long patientId,
//                                    @RequestParam("appointmentId") Long appointmentId) throws AppointmentNotFoundException {
//        return appointmentService.cancelAppointment(patientId, appointmentId);
//    }
//    @PostMapping("/{id}/cancelAppointment")
//    public String cancelAppointment(@PathVariable("id") Long patientId,
//                                    @RequestParam("appointmentId") Long appointmentId) throws AppointmentNotFoundException {
//        return appointmentService.cancelAppointment(patientId, appointmentId);
//    }
    @PostMapping("/cancelAppointment")
    public String cancelAppointment(@RequestParam("appointmentId") Long appointmentId, Authentication authentication) throws UserNotFoundException,PatientNotFoundException, AppointmentNotFoundException {
        return appointmentService.cancelAppointment(appointmentId, authentication);
    }
//    @GetMapping("/{id}/prescriptionHistory")
//    public List<Prescription> getPrescriptionHistory(@PathVariable("id") Long id) throws PatientNotFoundException{
//        return patientService.getPrescriptionHistory(id);
//    }
    @GetMapping("/prescriptionHistory")
    public List<Prescription> getPrescriptionHistory(Authentication authentication) throws UserNotFoundException, PatientNotFoundException{
        return patientService.getPrescriptionHistory(authentication);
    }
}
