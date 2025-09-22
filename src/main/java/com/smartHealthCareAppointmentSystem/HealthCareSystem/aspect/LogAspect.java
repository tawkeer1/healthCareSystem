package com.smartHealthCareAppointmentSystem.HealthCareSystem.aspect;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Appointment;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Doctor;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Patient;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.User;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.DoctorRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.UserRepo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LogAspect {
    private Logger logger = Logger.getLogger(LogAspect.class.getName());
    private final PatientRepo patientRepo;
    private final UserRepo userRepo;
    private final DoctorRepo doctorRepo;
    public LogAspect(PatientRepo patientRepo, UserRepo userRepo, DoctorRepo doctorRepo){
        this.patientRepo = patientRepo;
        this.userRepo  = userRepo;
        this.doctorRepo = doctorRepo;
    }
    @Around("execution(* com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AppointmentService.bookAppointment(..))")
    public Object logBooking(ProceedingJoinPoint joinPoint) throws Throwable{
        logger.info("Method: " + joinPoint.toString() + " execution started");
        Object[] args = joinPoint.getArgs();
        Authentication authentication = (Authentication) args[3];
        String email = authentication.getName();
        User user = userRepo.findByEmail(email);
        Patient patient = patientRepo.findByUserId(user.getId());
        Doctor doctor = (Doctor) args[0];
        logger.info("Appointment being booked for " + patient.getUser().getName() + " with doctor " + doctor.getUser().getName());
        Object result = joinPoint.proceed();
        logger.info("Appointment booked succesfully");
        logger.info("Method: " + joinPoint.toString() + " execution ended");
        return result;
    }
    @Around("execution(* com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AppointmentService.cancelAppointment(..))")
    public Object logCancelling(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Method: " + joinPoint.toString() + " execution started");
        Object[] args = joinPoint.getArgs();
        Authentication authentication = (Authentication) args[1];
        User user = userRepo.findByEmail(authentication.getName());
        Patient patient = patientRepo.findByUserId(user.getId());
        logger.info("Appointment " + args[0] + " being cancelled for patient " + patient.getUser().getName());
        Object result = joinPoint.proceed();
        logger.info("Appointment successfully cancelled");
        logger.info("Method: " + joinPoint.toString() + " execution ended");
        return result;
    }
    @Around("execution(* com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PrescriptionService.addPrescription(..))")
    public Object logPrescription(ProceedingJoinPoint joinPoint) throws Throwable{
        logger.info("Method " + joinPoint.toString() + " execution started");
        Object[] args = joinPoint.getArgs();
        Authentication authentication = (Authentication) args[3];
        User user = userRepo.findByEmail(authentication.getName());
        Doctor doctor = doctorRepo.findDoctorByUserId(user.getId());
        logger.info("Prescription being added by doctor: " + doctor.getUser().getName() + " to appointment " + args[1]);
        Object result = joinPoint.proceed();
        logger.info("Prescription added successfully");
        logger.info("Method: " + joinPoint.toString() + " execution ended");
        return result;
    }
}
