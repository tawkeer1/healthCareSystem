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
import org.ehcache.shadow.org.terracotta.offheapstore.disk.storage.AATreeFileAllocator;
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
    public Object logBooking(ProceedingJoinPoint joinPoint, Authentication authentication) throws Throwable{
        logger.info("Method: " + joinPoint.toString() + " execution started");
        Object[] args = joinPoint.getArgs();
        String email = authentication.getName();
        Patient patient = patientRepo.findByEmail(email);
        Doctor doctor = (Doctor) args[0];
        logger.info("Appointment being booked for " + patient.getName() + " with doctor " + doctor.getName());
        Object result = joinPoint.proceed();
        logger.info("Appointment booked succesfully");
        logger.info("Method: " + joinPoint.toString() + " execution ended");
        return result;
    }
    @Around("execution(* com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AppointmentService.cancelAppointment(..))")
    public Object logCancelling(ProceedingJoinPoint joinPoint, Authentication authentication) throws Throwable {
        logger.info("Method: " + joinPoint.toString() + " execution started");
        Object[] args = joinPoint.getArgs();
        Patient patient = patientRepo.findByEmail(authentication.getName());
        logger.info("Appointment " + args[0] + " being cancelled for patient " + patient.getName());
        Object result = joinPoint.proceed();
        logger.info("Appointment successfully cancelled");
        logger.info("Method: " + joinPoint.toString() + " execution ended");
        return result;
    }
    @Around("execution(* com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PrescriptionService.addPrescription(..))")
    public Object logPrescription(ProceedingJoinPoint joinPoint, Authentication authentication) throws Throwable{
        logger.info("Method " + joinPoint.toString() + " execution started");
        Object[] args = joinPoint.getArgs();
        Doctor doctor = doctorRepo.findByEmail(authentication.getName());
        logger.info("Prescription being added by doctor: " + doctor.getName() + " to appointment " + args[1]);
        Object result = joinPoint.proceed();
        logger.info("Prescription added successfully");
        logger.info("Method: " + joinPoint.toString() + " execution ended");
        return result;
    }
}
