package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AppointmentRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    private final AppointmentRepo appointmentRepo;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final UserRepo userRepo;
    private final PatientRepo patientRepo;
    @Autowired
    public AppointmentService(AppointmentRepo appointmentRepo, DoctorService doctorService,
                              PatientService patientService, UserRepo userRepo, PatientRepo patientRepo){
        this.appointmentRepo = appointmentRepo;
        this.doctorService = doctorService;
        this.patientService = patientService;
        this.userRepo = userRepo;
        this.patientRepo = patientRepo;
    }
    
//    public Appointment bookAppointment(Doctor doctor, Long id) throws PatientNotFoundException,DoctorNotFoundException, DoctorBusyException {
//        // what if multiple doctors with the speciality
//        Patient patient = patientService.findPatientById(id);
//        if(doctor == null) throw new DoctorNotFoundException("The doctor does not exist");
//        if(patient == null) throw new PatientNotFoundException("Patient does not exist");
//        //if doctor is busy
//        Appointment existingAppointment = appointmentRepo.findAppointmentByDoctorIdandStatusandTime(doctor.getId(),Status.BOOKED,LocalDateTime.now());
//        if(existingAppointment != null) throw new DoctorBusyException("Doctor is busy with another appointment");
//
//        Appointment appointment = new Appointment();
//        appointment.setDoctor(doctor);
//        appointment.setPatient(patient);
//        appointment.setTime(LocalDateTime.now());
//        appointment.setStatus(Status.BOOKED);
//        return appointmentRepo.save(appointment);
//    }

    public Appointment bookAppointment(Doctor doctor, LocalDateTime startTime, LocalDateTime endTime, Authentication authentication) throws UserNotFoundException, PatientNotFoundException,DoctorNotFoundException, DoctorBusyException {
        String email = authentication.getName();
        if(email == null) throw new UserNotFoundException("Login details not found. Please login");
        User user = userRepo.findByEmail(email);
        if(user == null) throw new UserNotFoundException("User details don't exist");
        Patient patient = patientRepo.findByUserId(user.getId());
        if(doctor == null) throw new DoctorNotFoundException("The doctor does not exist");
        if(patient == null) throw new PatientNotFoundException("Patient does not exist");
        if(startTime.isBefore(LocalDateTime.now()) || endTime.isBefore(LocalDateTime.now()) || startTime.isAfter(endTime))
         throw new RuntimeException("Please enter a valid time");
        //if doctor is busy
//        Appointment existingAppointment = appointmentRepo.findAppointmentByDoctorIdandStatusandTime(doctor.getId(),Status.BOOKED,LocalDateTime.now());
        List<Appointment> existingAppointments = appointmentRepo.findByDoctorIdAndStatus(doctor.getId(),Status.BOOKED);
        for(Appointment app : existingAppointments){
//            System.out.println(app);
            if(startTime.isBefore(app.getEndTime()) && endTime.isAfter(app.getStartTime())){
                throw new DoctorBusyException("Doctor is busy with another appointment");
            }
        }
        //increment appointment count
        doctor.setAppCount(doctor.getAppCount()+1);
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setTime(LocalDateTime.now());
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setStatus(Status.BOOKED);
        return appointmentRepo.save(appointment);
    }

//    public String cancelAppointment(Long patientId, Long appointmentId) throws AppointmentNotFoundException{
//        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
//        if(!appointment.isPresent()) throw new AppointmentNotFoundException("Appointment " + appointmentId + " not found");
//        if(appointment.get().getPatient().getId().equals(patientId){
//            throw new RuntimeException("Please enter your own valid appointment id");
//        }
//        if(appointment.get().getStatus() == Status.CANCELLED) throw new RuntimeException("Appointment is already cancelled");
//        appointment.get().setStatus(Status.CANCELLED);
//        appointmentRepo.save(appointment.get());
//        return "Appointment cancelled successfully";
//    }

    public String cancelAppointment(Long appointmentId, Authentication authentication) throws UserNotFoundException,PatientNotFoundException,AppointmentNotFoundException{
        String email = authentication.getName();
        if(email == null) throw new UserNotFoundException("Login details not found. Please login");
        User user = userRepo.findByEmail(email);
        if(user == null) throw new UserNotFoundException("User details don't exist");
        Patient patient = patientRepo.findByUserId(user.getId());
        if(patient == null) throw new PatientNotFoundException("Patient does not exist");
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if(!appointment.isPresent()) throw new AppointmentNotFoundException("Appointment " + appointmentId + " not found");
        if(appointment.get().getPatient().getId().equals(patient.getId())){
            throw new RuntimeException("Please enter your own valid appointment id");
        }
        if(appointment.get().getStatus() == Status.CANCELLED) throw new RuntimeException("Appointment is already cancelled");
        if(appointment.get().getStatus() == Status.COMPLETED) throw new RuntimeException("You cannot cancel a completed appointment");
        appointment.get().setStatus(Status.CANCELLED);
        appointmentRepo.save(appointment.get());
        return "Appointment cancelled successfully";
    }
}
