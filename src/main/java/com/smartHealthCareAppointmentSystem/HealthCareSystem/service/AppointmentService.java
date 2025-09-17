package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.AppointmentAlreadyBookedException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorBusyException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Appointment;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Status;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AppointmentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AppointmentService {
    private final AppointmentRepo appointmentRepo;
    private final DoctorService doctorService;
    @Autowired
    public AppointmentService(AppointmentRepo appointmentRepo, DoctorService doctorService){
        this.appointmentRepo = appointmentRepo;
        this.doctorService = doctorService;
    }
//    public Appointment bookAppointment(String speciality, Long id) throws DoctorNotFoundException, DoctorBusyException {
//        Doctor doctor = doctorService.searchDoctorBySpeciality(speciality);
//        if(doctor == null) throw new DoctorNotFoundException("No doctor with the speciality exists");
//        //if appointment is already booked
//        Appointment existingAppointment = appointmentRepo.findAppointmentByDoctorIdAndStatus(doctor.getId(),Status.BOOKED);
//        if(existingAppointment.getTime().getMinute() == LocalDateTime.now().getMinute()){
//            throw new DoctorBusyException("Doctor is busy");
//        }
//        Appointment appointment = new Appointment();
//        appointment.setDoctorId(doctor.getId());
//        appointment.setPatientId(id);
//        appointment.setTime(LocalDateTime.now());
//        appointment.setStatus(Status.BOOKED);
//        return appointmentRepo.save(appointment);
//    }
}
