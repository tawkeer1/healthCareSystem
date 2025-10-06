package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.dto.PatientRequest;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PatientService {
    private final PatientRepo patientRepo;
    private final PrescriptionService prescriptionService;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PatientService(PatientRepo patientRepo, PrescriptionService prescriptionService,
                          UserRepo userRepo, PasswordEncoder passwordEncoder){
        this.patientRepo = patientRepo;
        this.prescriptionService = prescriptionService;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }
    public Patient createPatient(Patient patient) throws UserAlreadyExistsException{
        User exisitingUser = userRepo.findByEmail(patient.getEmail());
        if(exisitingUser != null) throw new UserAlreadyExistsException("User with this email already exists");
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));
        patient.setRole(Role.PATIENT);
        return patientRepo.save(patient);
    }
    public Patient findByEmail(String email){
        return patientRepo.findByEmail(email);
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

    public Patient updatePatient(Long id, PatientRequest updatedPatient) throws PatientNotFoundException{
        Patient patient = patientRepo.findPatientById(id);
        if(updatedPatient == null ) throw new PatientNotFoundException("Updated details cannot be null");
        if(patient == null || patient.getId() == null) throw new PatientNotFoundException("Patient you want to update doesn't exist");

        if(updatedPatient.getAddress() != null){
            patient.setAddress(updatedPatient.getAddress());
        }
        if(updatedPatient.getFamilyHistory() != null){
            patient.setFamilyHistory(updatedPatient.getFamilyHistory());
        }
        if(updatedPatient.getMedicalHistory() != null){
            patient.setMedicalHistory(updatedPatient.getMedicalHistory());
        }
        if(updatedPatient.getName() != null){
            patient.setName(updatedPatient.getName());
        }
        return patientRepo.save(patient);
    }

    public Patient updatePersonalDetails(PatientRequest updatedPatient, Patient patient)
            throws PatientNotFoundException{

        if(patient == null) throw new PatientNotFoundException("Patient details not found");
        if(updatedPatient == null) throw new PatientNotFoundException("The details to be updated not found");

        if(updatedPatient.getName() != null) {
            patient.setName(updatedPatient.getName());
        }
        if(updatedPatient.getAddress() != null){
            patient.setAddress(updatedPatient.getAddress());
        }

        if(updatedPatient.getMedicalHistory() != null){
            patient.setMedicalHistory(updatedPatient.getMedicalHistory());
        }

        if(updatedPatient.getFamilyHistory() != null){
            patient.setFamilyHistory(updatedPatient.getFamilyHistory());
        }
        return patientRepo.save(patient);
    }

    public List<Prescription> getPrescriptionHistory(Patient patient){
        return prescriptionService.getPrescriptionHistory(patient.getId());
    }
}
