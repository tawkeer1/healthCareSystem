package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Appointment;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Patient;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Status;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AppointmentService {
    private final AppointmentRepo appointmentRepo;
    private final DoctorService doctorService;
    private final PatientService patientService;
    @Autowired
    public AppointmentService(AppointmentRepo appointmentRepo, DoctorService doctorService, PatientService patientService){
        this.appointmentRepo = appointmentRepo;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }
    public Appointment bookAppointment(Doctor doctor, Long id) throws PatientNotFoundException,DoctorNotFoundException, DoctorBusyException {
        // what if multiple doctors with the speciality
        Patient patient = patientService.findPatientById(id);
        if(doctor == null) throw new DoctorNotFoundException("The doctor does not exist");
        if(patient == null) throw new PatientNotFoundException("Patient does not exist");
        //if doctor is busy
        Appointment existingAppointment = appointmentRepo.findAppointmentByDoctorIdandStatusandTime(doctor.getId(),Status.BOOKED,LocalDateTime.now());
        if(existingAppointment != null) throw new DoctorBusyException("Doctor is busy with another appointment");

        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setTime(LocalDateTime.now());
        appointment.setStatus(Status.BOOKED);
        return appointmentRepo.save(appointment);
    }
    public String cancelAppointment(Long patientId, Long appointmentId) throws AppointmentNotFoundException{
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if(!appointment.isPresent()) throw new AppointmentNotFoundException("Appointment " + appointmentId + " not found");
        if(appointment.get().getPatient().getId() != patientId){
            throw new RuntimeException("Please enter your own valid appointment id");
        }
        if(appointment.get().getStatus() == Status.CANCELLED) throw new RuntimeException("Appointment is already cancelled");
        appointment.get().setStatus(Status.CANCELLED);
        appointmentRepo.save(appointment.get());
        return "Appointment cancelled successfully";
    }
}
