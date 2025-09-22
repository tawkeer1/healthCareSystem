package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final AppointmentRepo appointmentRepo;
    private final PrescriptionService prescriptionService;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public DoctorService(PatientRepo patientRepo,AppointmentRepo appointmentRepo, PrescriptionService prescriptionService
    ,DoctorRepo doctorRepo, UserRepo userRepo, PasswordEncoder passwordEncoder){
        this.appointmentRepo = appointmentRepo;
        this.prescriptionService = prescriptionService;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Doctor createDoctor(Doctor doctor) throws DoctorNotFoundException, UserAlreadyExistsException{
        User exisitingUser = userRepo.findByEmail(doctor.getUser().getEmail());
        if(exisitingUser != null) throw new UserAlreadyExistsException("User with this email already exists");
        if(doctor == null) throw new DoctorNotFoundException("Doctor details cannot be null");
        doctor.getUser().setPassword(passwordEncoder.encode(doctor.getUser().getPassword()));
        doctor.getUser().setRole(Role.DOCTOR);
        return doctorRepo.save(doctor);
    }

    public Doctor createDoctorIfUserExists(DoctorRequest doctor, Long userId) throws UserNotFoundException{
        Optional<User> user = userRepo.findById(userId);
        if(!user.isPresent()) throw new UserNotFoundException("User not found");
        if(user.get().getId() == null) throw new UserNotFoundException("User does not exist");
        //if this user is already linked to someone
        Patient patient = patientRepo.findByUserId(userId);
        Doctor existingDoctor = doctorRepo.findDoctorByUserId(userId);
        if(patient != null || existingDoctor != null) throw new RuntimeException("User is already linked to someone");
        Doctor newDoctor = new Doctor();
        newDoctor.setSpeciality(doctor.getSpeciality());
        newDoctor.setLicenseNumber(doctor.getLicenseNumber());
        user.get().setRole(Role.DOCTOR);
        newDoctor.setUser(user.get());
        return doctorRepo.save(newDoctor);
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

    public Doctor updatePersonalDetails(DoctorRequest doctorRequest, Authentication authentication) throws UserNotFoundException, DoctorNotFoundException{
        String email = authentication.getName();
        if(email == null) throw new UserNotFoundException("Login details not found");
        User user = userRepo.findByEmail(email);
        if(user == null) throw new UserNotFoundException("User details not found");
        Doctor doctor = doctorRepo.findDoctorByUserId(user.getId());
        if(doctor == null) throw new DoctorNotFoundException("Doctor details not found");
        if(doctorRequest == null) throw new RuntimeException("The updated doctor details cannot be null");

//        if(!(authentication.getName().equals(doctor.getUser().getEmail()))){
//            throw new UnauthorizedUserException("You can only update your details");
//        }

        if(doctorRequest.getName() != null){
            doctor.getUser().setName(doctorRequest.getName());
        }

        if(doctorRequest.getSpeciality() != null){
            doctor.setSpeciality(doctorRequest.getSpeciality());
        }
        if(doctorRequest.getLicenseNumber() != null){
            doctor.setLicenseNumber(doctorRequest.getLicenseNumber());
        }
        return doctorRepo.save(doctor);
    }
    public Doctor updateDoctor(Long id, DoctorRequest updatedDoctor, Authentication authentication) throws DoctorNotFoundException, UnauthorizedUserException{
        Doctor doctor = doctorRepo.findDoctorById(id);
        if(updatedDoctor == null) throw new DoctorNotFoundException("Enter a valid updated doctor");
        if(doctor == null || doctor.getId() == null) throw new DoctorNotFoundException("Doctor you want to update is not found");

        if(updatedDoctor.getSpeciality() != null) {
            doctor.setSpeciality(updatedDoctor.getSpeciality());
        }
        if(updatedDoctor.getLicenseNumber() != null){
            doctor.setLicenseNumber(updatedDoctor.getLicenseNumber());
        }
        if(updatedDoctor.getName() != null){
            doctor.getUser().setName(updatedDoctor.getName());
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
    public String markAppointmentAsCompleted(Long appointmentId,Authentication authentication) throws UserNotFoundException, DoctorNotFoundException, AppointmentNotFoundException{
        String email = authentication.getName();
        if(email == null) throw new RuntimeException("Login details not found. Please login");
        User user = userRepo.findByEmail(email);
        if(user == null) throw new UserNotFoundException("Your user account doesn't exist");
        Doctor doctor = doctorRepo.findDoctorByUserId(user.getId());
        if(doctor == null) throw new DoctorNotFoundException("Doctor you want to mark appointment doesn't exist");
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if(!appointment.isPresent()) throw new AppointmentNotFoundException("Appointment to be marked doesn't exist");
        if(appointment.get().getDoctor().getId() != doctor.getId()){
            throw new RuntimeException("Please enter your own valid appointment to mark");
        }
        appointment.get().setStatus(Status.COMPLETED);
        appointmentRepo.save(appointment.get());
        return "Appointment was marked as completed successfully";
    }

    public Prescription addPrescription(Long appointmentId, Medication medication, Authentication authentication) throws UserNotFoundException, DoctorNotFoundException, AppointmentNotFoundException{
        String email = authentication.getName();
        if(email == null) throw new RuntimeException("Login details not found. Please login");
        User user = userRepo.findByEmail(email);
        if(user == null) throw new UserNotFoundException("Your user account doesn't exist");
        Doctor doctor = doctorRepo.findDoctorByUserId(user.getId());
        if(doctor == null) throw new DoctorNotFoundException("No doctor details found");
        return prescriptionService.addPrescription(doctor.getId(),appointmentId,medication,authentication);
    }

    public List<Appointment> getAllAppointments(Authentication authentication) throws UserNotFoundException{
        String email = authentication.getName();
        if(email == null) throw new UserNotFoundException("Login details not found");
        User user = userRepo.findByEmail(email);
        if(user == null) throw new UserNotFoundException("User details not found");
        Doctor doctor = doctorRepo.findDoctorByUserId(user.getId());
        return appointmentRepo.findByDoctorId(doctor.getId());
    }
    public List<Appointment> getTodayAppointments(Authentication authentication) throws UserNotFoundException{
        String email = authentication.getName();
        if(email == null) throw new RuntimeException("Login details not found. Please login");
        User user = userRepo.findByEmail(email);
        if(user == null) throw new UserNotFoundException("User details not found");
        Doctor doctor = doctorRepo.findDoctorByUserId(user.getId());

        LocalDate time = LocalDate.now();
        LocalDateTime startOfDay = time.atStartOfDay();
        LocalDateTime endOfDay = time.atTime(LocalTime.MAX);
//        List<Appointment> appointments = appointmentRepo.findAll();
//        List<Appointment> todayAppointments = new ArrayList<>();
        return appointmentRepo.findByDoctorIdAndStatusAndTimeBetween(
                doctor.getId(), Status.BOOKED, startOfDay, endOfDay
        );
    }
    public List<Doctor> mostFrequentlyUsedDoctors(){
        Pageable topThree = PageRequest.of(0,3);
        return doctorRepo.findMostFrequentlyUsedDoctors(topThree);
    }
}
