package com.smartHealthCareAppointmentSystem.HealthCareSystem.service;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.dto.DoctorRequest;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.Doc;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class DoctorService {
    private final AppointmentRepo appointmentRepo;
    private final PrescriptionService prescriptionService;
    private final DoctorRepo doctorRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DoctorService(AppointmentRepo appointmentRepo, PrescriptionService prescriptionService
            , DoctorRepo doctorRepo, UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.appointmentRepo = appointmentRepo;
        this.prescriptionService = prescriptionService;
        this.doctorRepo = doctorRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public Doctor createDoctor(Doctor doctor) throws UserAlreadyExistsException {
        User exisitingUser = userRepo.findByEmail(doctor.getEmail());
        if (exisitingUser != null) throw new UserAlreadyExistsException("User with this email already exists");
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctor.setRole(Role.DOCTOR);
        return doctorRepo.save(doctor);
    }

    public Doctor findDoctorById(Long id) {
        return doctorRepo.findDoctorById(id);
    }

    public String deleteDoctor(Long id) throws DoctorNotFoundException {
        Doctor doctor = doctorRepo.findDoctorById(id);
        if (doctor == null || doctor.getId() == null)
            throw new DoctorNotFoundException("Doctor you want to delete doesn't exist");
        doctorRepo.deleteById(id);
        return "Deleted doctor successfully";
    }

    public Doctor updatePersonalDetails(DoctorRequest doctorRequest, Doctor doctor) throws DoctorNotFoundException {

        if (doctor == null) throw new DoctorNotFoundException("Doctor details not found");

        if (doctorRequest.getSpeciality() != null) {
            doctor.setSpeciality(doctorRequest.getSpeciality());
        }
        if (doctorRequest.getName() != null) {
            doctor.setName(doctorRequest.getName());
        }
        if (doctorRequest.getLicenseNumber() != null) {
            doctor.setLicenseNumber(doctorRequest.getLicenseNumber());
        }

        return doctorRepo.save(doctor);
    }

    public Doctor updateDoctor(Long id, DoctorRequest updatedDoctor) throws DoctorNotFoundException{
        Doctor doctor = doctorRepo.findDoctorById(id);
        if (doctor == null || doctor.getId() == null)
            throw new DoctorNotFoundException("Doctor you want to update is not found");
        doctor.setSpeciality(updatedDoctor.getSpeciality());
        doctor.setLicenseNumber(updatedDoctor.getLicenseNumber());
        doctor.setName(updatedDoctor.getName());
        return doctorRepo.save(doctor);
    }

    public Doctor searchDoctorBySpeciality(String speciality) throws DoctorNotFoundException {
        Doctor doctor = doctorRepo.findDoctorBySpeciality(speciality);
        if (doctor == null) throw new DoctorNotFoundException("Doctor you are searching for doesn't exist");
        return doctor;
    }

    public List<Doctor> searchDoctorsBySpeciality(String speciality) {
        return doctorRepo.findDoctorsBySpeciality(speciality);
    }

    public String markAppointmentAsCompleted(Doctor doctor, Long appointmentId)
            throws DoctorNotFoundException,
            AppointmentNotFoundException, NotValidAppointmentException {
        if (doctor == null) throw new DoctorNotFoundException("Doctor you want to mark appointment doesn't exist");
        Optional<Appointment> appointment = appointmentRepo.findById(appointmentId);
        if (!appointment.isPresent()) throw new AppointmentNotFoundException("Appointment to be marked doesn't exist");
        if (!(appointment.get().getDoctor().getId().equals(doctor.getId()))) {
            throw new NotValidAppointmentException("Please enter your own valid appointment to mark");
        }
        appointment.get().setStatus(Status.COMPLETED);
        appointmentRepo.save(appointment.get());
        return "Appointment was marked as completed successfully";
    }

    public Prescription addPrescription(Long appointmentId, Doctor doctor, Medication medication) throws
            DoctorNotFoundException, PrescriptionAlreadyAddedException, NotValidAppointmentActionException,
            AppointmentNotFoundException {
        if (doctor == null) throw new DoctorNotFoundException("No doctor details found");
        return prescriptionService.addPrescription(doctor, appointmentId, medication);
    }

    public List<Appointment> getAllAppointments(Doctor doctor){
        return appointmentRepo.findByDoctorId(doctor.getId());
    }

    public List<Appointment> getTodayAppointments(Doctor doctor){
        LocalDate time = LocalDate.now();
        LocalDateTime startOfDay = time.atStartOfDay();
        LocalDateTime endOfDay = time.atTime(LocalTime.MAX);
        return appointmentRepo.findByDoctorIdAndStatusAndTimeBetween(
                doctor.getId(), Status.BOOKED, startOfDay, endOfDay
        );
    }

    public List<Doctor> mostFrequentlyUsedDoctors() {
        Pageable topThree = PageRequest.of(0, 3);
        return doctorRepo.findMostFrequentlyUsedDoctors(topThree);
    }
}
