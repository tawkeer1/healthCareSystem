package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.AppointmentNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.UserNotFoundException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Doctor createDoctor(Doctor doctor) throws DoctorNotFoundException{
        if(doctor == null) throw new DoctorNotFoundException("Doctor details cannot be null");
        doctor.getUser().setPassword(passwordEncoder.encode(doctor.getUser().getPassword()));
        doctor.getUser().setRole(Role.DOCTOR);
        return doctorRepo.save(doctor);
    }

    public Doctor createDoctorIfUserExists(DoctorRequest doctor, Long userId) throws UserNotFoundException, DoctorNotFoundException{
        Optional<User> user = userRepo.findById(userId);
        if(!user.isPresent()) throw new UserNotFoundException("User not found");
        if(user.get().getId() == null) throw new UserNotFoundException("User does not exist");
        //if this user is already linked to someone
        Patient patient = patientRepo.findPatientByUserId(userId);
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

    public Prescription addPrescription(Long doctorId, Long appointmentId, Medication medication) throws DoctorNotFoundException, AppointmentNotFoundException{
        return prescriptionService.addPrescription(doctorId,appointmentId,medication);
    }

    public List<Appointment> getAllAppointments(Long id){
        return appointmentRepo.findByDoctorId(id);
    }
    public List<Appointment> getTodayAppointments(Long doctorId){
        LocalDate time = LocalDate.now();
        LocalDateTime startOfDay = time.atStartOfDay();
        LocalDateTime endOfDay = time.atTime(LocalTime.MAX);
        List<Appointment> appointments = appointmentRepo.findAll();
        List<Appointment> todayAppointments = new ArrayList<>();
        for(Appointment app : appointments){
            if(app.getDoctor().getId() == doctorId && app.getStatus() == Status.BOOKED && app.getTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()){
                todayAppointments.add(app);
            }
        }
        return todayAppointments;
    }
}
