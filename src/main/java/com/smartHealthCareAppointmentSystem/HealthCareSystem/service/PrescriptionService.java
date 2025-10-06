package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.AppointmentNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.NotValidAppointmentActionException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.PrescriptionAlreadyAddedException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AppointmentRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PrescriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {
    private final PrescriptionRepo prescriptionRepo;
    private final AppointmentRepo appointmentRepo;
    @Autowired
    public PrescriptionService(PrescriptionRepo prescriptionRepo, AppointmentRepo appointmentRepo){
        this.prescriptionRepo = prescriptionRepo;
        this.appointmentRepo = appointmentRepo;
    }
    //authenication is used in aop please don't remove
    public Prescription addPrescription(Doctor doctor, Long appointmentId, Medication medication) throws
            AppointmentNotFoundException, PrescriptionAlreadyAddedException, NotValidAppointmentActionException {
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if(!appointment.isPresent()) throw new AppointmentNotFoundException("Appointment doesn't exist");
        if(!(doctor.getId().equals(appointment.get().getDoctor().getId()))) throw new NotValidAppointmentActionException("Please select your own appointment to add prescription");
        if(appointment.get().getStatus() == Status.COMPLETED) throw new PrescriptionAlreadyAddedException("The prescription has already been added by the doctor");
        if(appointment.get().getStatus() == Status.CANCELLED) throw new NotValidAppointmentActionException("The appointment has already been cancelled");

        Prescription prescription = new Prescription();
        prescription.setDoctorId(doctor.getId());
        prescription.setAppointmentId(appointment.get().getId());
        prescription.setPatientId(appointment.get().getPatient().getId());
        prescription.setIssueTime(LocalDateTime.now());
        prescription.setMedicines(medication.getMedicines());
        prescription.setDosage(medication.getDosage());
        prescription.setLabReports(medication.getLabReports());
        //once doctor adds prescription the appointment can be said to be complete
        appointment.get().setStatus(Status.COMPLETED);
        appointmentRepo.save(appointment.get());
        return prescriptionRepo.save(prescription);
    }

    public List<Prescription> getPrescriptionHistory(Long patientId){
        return prescriptionRepo.findByPatientId(patientId);
    }
}
