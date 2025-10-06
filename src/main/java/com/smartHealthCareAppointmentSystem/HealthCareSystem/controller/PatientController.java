package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.dto.AppointmentRequest;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.dto.PatientRequest;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.dto.SpecialityAppRequest;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AppointmentService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PatientService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.UserContextService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserContextService userContextService;

    @Autowired
    public PatientController(DoctorService doctorService, PatientService patientService,
                             AppointmentService appointmentService, UserContextService userContextService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.appointmentService = appointmentService;
        this.userContextService = userContextService;
    }

    @GetMapping("/searchDoctor")
    public Doctor searchDoctor(@RequestParam("speciality") String speciality) throws DoctorNotFoundException {
        return doctorService.searchDoctorBySpeciality(speciality);
    }

    @PatchMapping("/updatePersonalDetails")
    public Patient updatePersonalDetails(@RequestBody PatientRequest updatedPatient, Authentication authentication) throws PatientNotFoundException, InvalidLoginDetailsException {
        Patient patient = userContextService.getAuthenticatedPatient(authentication);
        return patientService.updatePersonalDetails(updatedPatient, patient);
    }

    @PostMapping("/bookAppWithSpeciality")
    public Appointment bookAppointment(@Valid @RequestBody SpecialityAppRequest request,
                                       Authentication authentication) throws
            DoctorNotFoundException, DoctorBusyException, NotValidTimeException, InvalidLoginDetailsException {
        String speciality = request.getSpeciality();
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();
        List<Doctor> doctors = doctorService.searchDoctorsBySpeciality(speciality);
        if (doctors.isEmpty()) throw new DoctorNotFoundException("No doctor with the speciality exists");
        // select a random doctor from the list
        int size = doctors.size();
        Doctor doctor = doctors.get(new Random().nextInt(0, size));
        Patient patient = userContextService.getAuthenticatedPatient(authentication);
        return appointmentService.bookAppointment(doctor, patient, startTime, endTime);
    }

    @PostMapping("/bookAppointment")
    public Appointment bookAppointmentWithDoctor(@Valid @RequestBody AppointmentRequest appointmentRequest,
                                                 Authentication authentication) throws
            DoctorBusyException, NotValidTimeException, InvalidLoginDetailsException, DoctorNotFoundException {
        Long doctorId = appointmentRequest.getDoctorId();
        LocalDateTime startTime = appointmentRequest.getStartTime();
        LocalDateTime endTime = appointmentRequest.getEndTime();
        Doctor doctor = doctorService.findDoctorById(doctorId);
        if (doctor == null) throw new DoctorNotFoundException("Doctor does not exist");
        Patient patient = userContextService.getAuthenticatedPatient(authentication);
        return appointmentService.bookAppointment(doctor, patient, startTime, endTime);
    }

    @PostMapping("/cancelAppointment")
    public String cancelAppointment(@RequestParam("appointmentId") Long appointmentId, Authentication authentication) throws
            AppointmentNotFoundException, NotValidAppointmentException,
            NotValidAppointmentActionException, InvalidLoginDetailsException {
        Patient patient = userContextService.getAuthenticatedPatient(authentication);
        return appointmentService.cancelAppointment(appointmentId, patient);
    }

    @GetMapping("/prescriptionHistory")
    public List<Prescription> getPrescriptionHistory(Authentication authentication) throws InvalidLoginDetailsException {
        Patient patient = userContextService.getAuthenticatedPatient(authentication);
        return patientService.getPrescriptionHistory(patient);
    }
}
