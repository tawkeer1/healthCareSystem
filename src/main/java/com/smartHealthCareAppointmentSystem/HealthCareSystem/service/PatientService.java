package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.PatientNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PrescriptionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepo patientRepo;
    private final PrescriptionService prescriptionService;
    @Autowired
    public PatientService(PatientRepo patientRepo, PrescriptionService prescriptionService){
        this.patientRepo = patientRepo;
        this.prescriptionService = prescriptionService;
    }
    public ResponseEntity<Response> createPatient(Patient patient){
        if(patient == null ) throw new NullPointerException("Patient cannot be null");
        Response response = new Response("200","createdUserSuccessfully");
        return ResponseEntity.ok(response);
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
