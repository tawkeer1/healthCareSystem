package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Patient;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.User;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AdminRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.DoctorRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.util.List;

@Service
public class AdminService {
    private final AdminRepo adminRepo;
    private final PatientRepo patientRepo;
    private final DoctorRepo doctorRepo;
    @Autowired
    public AdminService(AdminRepo adminRepo, PatientRepo patientRepo, DoctorRepo doctorRepo) {
        this.adminRepo = adminRepo;
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
    }
    public List<Patient> getAllPatients(int pageNum){
        int size = 2;
        Pageable pageable = PageRequest.of(pageNum-1,size);
        Page<Patient> patientPage =patientRepo.findAll(pageable);
        List<Patient> patientContent = patientPage.getContent();
        return patientContent;
    }
    public List<Doctor> getAllDoctors(int pageNum){
        int size = 2;
        Pageable pageable = PageRequest.of(pageNum-1,size);
        Page<Doctor> doctorPage = doctorRepo.findAll(pageable);
        List<Doctor> doctorsContent = doctorPage.getContent();
        return doctorsContent;
    }
}
