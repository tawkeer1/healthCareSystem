package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Patient;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.DoctorRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AdminService {
    private final PatientRepo patientRepo;
    private final DoctorRepo doctorRepo;
    @Autowired
    public AdminService(PatientRepo patientRepo, DoctorRepo doctorRepo) {
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
    }
    public List<Patient> getAllPatients(int pageNum){
        int size = 2;
        Pageable pageable = PageRequest.of(pageNum-1,size);
        Page<Patient> patientPage =patientRepo.findAll(pageable);
        return patientPage.getContent();
    }
    public List<Doctor> getAllDoctors(int pageNum){
        int size = 2;
        Pageable pageable = PageRequest.of(pageNum-1,size);
        Page<Doctor> doctorPage = doctorRepo.findAll(pageable);
        return doctorPage.getContent();
    }
}
