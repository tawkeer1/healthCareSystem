package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.AppointmentNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AppointmentRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.DoctorRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PrescriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final AppointmentRepo appointmentRepo;
    private final PrescriptionService prescriptionService;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    @Autowired
    public DoctorService(PatientRepo patientRepo,AppointmentRepo appointmentRepo, PrescriptionService prescriptionService
    ,DoctorRepo doctorRepo){
        this.appointmentRepo = appointmentRepo;
        this.prescriptionService = prescriptionService;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
    }

    public Doctor createDoctor(Doctor doctor){
        return doctorRepo.save(doctor);
    }

    public Doctor findDoctorById(Long id){
        return doctorRepo.findDoctorById(id);
    }

    public String deleteDoctor(Long id) throws DoctorNotFoundException{
        Doctor doctor = doctorRepo.findDoctorById(id);
        if(doctor ==  null || doctor.getId() == null) throw new DoctorNotFoundException("Doctor you want to delete doesn't exist");
        doctorRepo.deleteById(id);
        return "Deleted doctor successfully";
    }

    public Doctor updateDoctor(Long id, Doctor updatedDoctor) throws DoctorNotFoundException{
        Doctor doctor = doctorRepo.findDoctorById(id);
        User doctorUser = doctor.getUser();
        User updatedDoctorUser = updatedDoctor.getUser();
        if(updatedDoctor == null) throw new DoctorNotFoundException("Enter a valid updated doctor");
        if(doctor == null || doctor.getId() == null) throw new DoctorNotFoundException("Doctor you want to update is not found");
        if(doctorUser == null || doctorUser.getId() == null) throw new DoctorNotFoundException("Doctor user not found");

        if(updatedDoctor.getSpeciality() != null) {
            doctor.setSpeciality(updatedDoctor.getSpeciality());
        }
        if(updatedDoctor.getLicenseNumber() != null){
            doctor.setLicenseNumber(updatedDoctor.getLicenseNumber());
        }
        if(updatedDoctorUser != null && updatedDoctor.getUser().getName() != null) {
            doctorUser.setName(updatedDoctor.getUser().getName());
        }
        if(updatedDoctorUser != null && updatedDoctor.getUser().getEmail() != null) {
            doctorUser.setEmail(updatedDoctor.getUser().getEmail());
        }
        if(updatedDoctorUser != null && updatedDoctor.getUser().getPassword() != null){
            doctorUser.setPassword(updatedDoctor.getUser().getPassword());
        }
        doctorRepo.save(doctor);
        return doctor;
    }

    public Doctor searchDoctorBySpeciality(String speciality) throws DoctorNotFoundException{
        Doctor doctor = doctorRepo.findDoctorBySpeciality(speciality);
        if(doctor == null) throw new DoctorNotFoundException("Doctor you are searching for doesn't exist");
        return doctor;
    }
    public List<Doctor> searchDoctorsBySpeciality(String speciality){
        return doctorRepo.findDoctorsBySpeciality(speciality);
    }
    public String markAppointmentAsCompleted(Long doctorId, Long appointmentId) throws DoctorNotFoundException, AppointmentNotFoundException{
        Doctor doctor = doctorRepo.findDoctorById(doctorId);
        if(doctor == null) throw new DoctorNotFoundException("Doctor you want to mark appointment doesn't exist");
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if(!appointment.isPresent()) throw new AppointmentNotFoundException("Appointment to be marked doesn't exist");
        if(appointment.get().getDoctor().getId() != doctorId){
            throw new RuntimeException("Please enter your own valid appointment id to mark");
        }
        appointment.get().setStatus(Status.COMPLETED);
        appointmentRepo.save(appointment.get());
        return "Appointment was marked as completed successfully by doctor " + doctorId;
    }

    public Prescription addPresciption(Long doctorId, Long appointmentId, Medication medication) throws DoctorNotFoundException, AppointmentNotFoundException{
        return prescriptionService.addPrescription(doctorId,appointmentId,medication);
    }
}
