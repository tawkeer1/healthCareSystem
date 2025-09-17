package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.User;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.DoctorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    private final DoctorRepo doctorRepo;
    @Autowired
    public DoctorService(DoctorRepo doctorRepo){
        this.doctorRepo = doctorRepo;
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
}
