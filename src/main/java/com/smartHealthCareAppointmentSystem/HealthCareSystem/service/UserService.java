package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.UserNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Patient;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Role;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.User;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.DoctorRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    @Autowired
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoder, DoctorRepo doctorRepo, PatientRepo patientRepo){
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
    }
    public User createUser(User user){
        System.out.println(user.getRole());
        user.setRole(Role.ANONY);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getRole());
        return userRepo.save(user);
    }
    @Transactional
    public String deleteUser(Long id) throws UserNotFoundException{
        Optional<User> user = userRepo.findById(id);
        if(!user.isPresent()) throw new UserNotFoundException("User doesn't exist");
        Doctor doctor = doctorRepo.findDoctorByUserId(id);
        if(doctor != null) doctorRepo.deleteById(doctor.getId());
        Patient patient = patientRepo.findByUserId(id);
        if(patient != null) patientRepo.deleteById(patient.getId());

        userRepo.deleteById(id);
        return "User deleted successfully";
    }
}
