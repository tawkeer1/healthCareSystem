package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AppointmentRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {
    private final AppointmentRepo appointmentRepo;
    private final DoctorRepo doctorRepo;
    @Autowired
    public AppointmentService(AppointmentRepo appointmentRepo,
                              DoctorRepo doctorRepo){
        this.appointmentRepo = appointmentRepo;
        this.doctorRepo = doctorRepo;
    }

    public Appointment bookAppointment(Doctor doctor,Patient patient, LocalDateTime startTime, LocalDateTime endTime)
            throws DoctorBusyException, NotValidTimeException {

        if(startTime.isBefore(LocalDateTime.now()) || endTime.isBefore(LocalDateTime.now()) || startTime.isAfter(endTime))
         throw new NotValidTimeException("Please enter a valid time");
        //if doctor is busy
        List<Appointment> existingAppointments = appointmentRepo.findByDoctorIdAndStatus(doctor.getId(),Status.BOOKED);
        for(Appointment app : existingAppointments){
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
        doctorRepo.save(doctor);
        return appointmentRepo.save(appointment);
    }


    public String cancelAppointment(Long appointmentId, Patient patient)
            throws
            AppointmentNotFoundException, NotValidAppointmentException, NotValidAppointmentActionException{

        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if(!appointment.isPresent()) throw new AppointmentNotFoundException("Appointment " + appointmentId + " not found");
        if(!(appointment.get().getPatient().getId().equals(patient.getId()))){
            throw new NotValidAppointmentException("Please enter your own valid appointment id");
        }
        if(appointment.get().getStatus() == Status.CANCELLED) throw new NotValidAppointmentActionException("Appointment is already cancelled");
        if(appointment.get().getStatus() == Status.COMPLETED) throw new NotValidAppointmentActionException("You cannot cancel a completed appointment");
        appointment.get().setStatus(Status.CANCELLED);
        appointmentRepo.save(appointment.get());
        return "Appointment cancelled successfully";
    }
}
