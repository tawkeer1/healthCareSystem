package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.PatientNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.UserNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.DoctorRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PrescriptionRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepo patientRepo;
    private final PrescriptionService prescriptionService;
    private final UserRepo userRepo;
    private final DoctorRepo doctorRepo;
    @Autowired
    public PatientService(PatientRepo patientRepo, PrescriptionService prescriptionService, UserRepo userRepo, DoctorRepo doctorRepo){
        this.patientRepo = patientRepo;
        this.prescriptionService = prescriptionService;
        this.userRepo = userRepo;
        this.doctorRepo = doctorRepo;
    }
    public Patient createPatient(Patient patient){
        if(patient == null ) throw new NullPointerException("Patient details cannot be null");
        patient.getUser().setRole(Role.PATIENT);
        return patientRepo.save(patient);
    }

    public Patient createPatientIfUserExists(PatientRequest patientRequest, Long userId) throws UserNotFoundException{
        Optional<User> user = userRepo.findById(userId);
        if(!user.isPresent()) throw new UserNotFoundException("User not found");
        if(user.get().getId() == null) throw new UserNotFoundException("User does not exist");
        //if this user is already linked to someone
        Patient patient = patientRepo.findPatientByUserId(userId);
        Doctor existingDoctor = doctorRepo.findDoctorByUserId(userId);
        if(patient != null || existingDoctor != null) throw new RuntimeException("User is already linked to someone");
        Patient newPatient = new Patient();
        newPatient.setAddress(patientRequest.getAddress());
        newPatient.setMedicalHistory(patientRequest.getMedicalHistory());
        newPatient.setFamilyHistory(patientRequest.getFamilyHistory());
        user.get().setRole(Role.PATIENT);
        newPatient.setUser(user.get());
        return patientRepo.save(newPatient);
    }
    public Patient findPatientById(Long id){
        return patientRepo.findPatientById(id);
    }

    public String deletePatient(Long id) throws PatientNotFoundException {
        Patient patient = patientRepo.findPatientById(id);
        if(patient ==  null || patient.getId() == null) throw new PatientNotFoundException("Patient you want to delete doesn't exist");
        patientRepo.deleteById(id);
        return "Deleted Patient successfully";
    }

    public Patient updatePatient(Long id, Patient updatedPatient) throws PatientNotFoundException{
        Patient patient = patientRepo.findPatientById(id);
        User patientUser = patient.getUser();
        User updatedPatientUser = updatedPatient.getUser();
        if(updatedPatient == null ) throw new NullPointerException("Updated patient cannot be null");
        if(patient == null || patient.getId() == null) throw new PatientNotFoundException("Patient you want to update doesn't exist");
        if(patientUser == null) throw new PatientNotFoundException("Patient User doesn't exist");
        if(updatedPatient.getAddress() != null){
            patient.setAddress(updatedPatient.getAddress());
        }
        if(updatedPatient.getFamilyHistory() != null){
            patient.setFamilyHistory(updatedPatient.getFamilyHistory());
        }
        if(updatedPatient.getMedicalHistory() != null){
            patient.setMedicalHistory(updatedPatient.getMedicalHistory());
        }

        if(updatedPatientUser != null && updatedPatientUser.getName() != null){
            patientUser.setName(updatedPatientUser.getName());
        }
        if(updatedPatientUser != null && updatedPatientUser.getEmail() != null){
            patientUser.setEmail(updatedPatientUser.getEmail());
        }
        if(updatedPatientUser != null && updatedPatientUser.getPassword() != null){
            patientUser.setPassword(updatedPatientUser.getPassword());
        }
        return patientRepo.save(patient);
    }
    public Patient updatePersonalDetails(Long id, Patient updatedPatient) throws PatientNotFoundException{
        Patient patient = patientRepo.findPatientById(id);
        if(patient == null) throw new PatientNotFoundException("Your details not found");
        if(updatedPatient == null) throw new PatientNotFoundException("The details to be updated not found");
        if(updatedPatient.getAddress() != null){
            patient.setAddress(updatedPatient.getAddress());
        } else {
            throw new RuntimeException("The updated address does not exist");
        }

        if(updatedPatient.getMedicalHistory() != null){
            patient.setMedicalHistory(updatedPatient.getMedicalHistory());
        } else {
            throw new RuntimeException("The updated medical history does not exist");
        }

        if(updatedPatient.getFamilyHistory() != null){
            patient.setFamilyHistory(updatedPatient.getFamilyHistory());
        } else {
            throw new RuntimeException("The updated family History does not exist");
        }
        return patientRepo.save(patient);
    }

    public List<Prescription> getPrescriptionHistory(Long id) throws PatientNotFoundException{
        Patient patient = patientRepo.findPatientById(id);
        if(patient == null) throw new PatientNotFoundException("Patient not found");
        return prescriptionService.getPrescriptionHistory(id);
    }
}
