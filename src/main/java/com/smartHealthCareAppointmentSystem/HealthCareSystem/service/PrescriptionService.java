package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.AppointmentNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AppointmentRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.DoctorRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PrescriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PrescriptionService {
    private final PrescriptionRepo prescriptionRepo;
    private final DoctorRepo doctorRepo;
    private final AppointmentRepo appointmentRepo;
    @Autowired
    public PrescriptionService(PrescriptionRepo prescriptionRepo, DoctorRepo doctorRepo, AppointmentRepo appointmentRepo){
        this.prescriptionRepo = prescriptionRepo;
        this.doctorRepo = doctorRepo;
        this.appointmentRepo = appointmentRepo;
    }
    public Prescription addPrescription(Long doctorId, Long appointmentId, Medication medication) throws AppointmentNotFoundException, DoctorNotFoundException {
        Doctor doctor = doctorRepo.findDoctorById(doctorId);
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if(!appointment.isPresent()) throw new AppointmentNotFoundException("Appointment doesn't exist");
        if(doctor == null) throw new DoctorNotFoundException("Doctor you want to add prescription doesn't exist");
        if(doctor.getId() != appointment.get().getDoctor().getId()) throw new RuntimeException("Please select your own appointment to add prescription");
        Prescription prescription = new Prescription();
        prescription.setDoctorId(doctor.getId());
        prescription.setAppointmentId(appointment.get().getId());
        prescription.setPatientId(appointment.get().getPatient().getId());
        prescription.setIssueTime(LocalDateTime.now());
        prescription.setMedicines(medication.getMedicines());
        prescription.setDosage(medication.getDosage());
        prescription.setLabReports(medication.getLabReports());
        return prescriptionRepo.save(prescription);
    }

    public List<Prescription> getPrescriptionHistory(Long patientId){
        return prescriptionRepo.findByPatientId(patientId);
    }
}
