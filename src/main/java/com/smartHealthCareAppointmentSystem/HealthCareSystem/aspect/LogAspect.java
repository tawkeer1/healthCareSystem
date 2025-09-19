package com.smartHealthCareAppointmentSystem.HealthCareSystem.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Aspect
@Component
public class LogAspect {
    private Logger logger = Logger.getLogger(LogAspect.class.getName());

    @Around("execution(* com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AppointmentService.bookAppointment(..))")
    public Object logBooking(ProceedingJoinPoint joinPoint) throws Throwable{
        logger.info("Method: " + joinPoint.toString() + " execution started");
        Object[] args = joinPoint.getArgs();
        logger.info("Appointment being booked for " + args[1].toString());
        Object result = joinPoint.proceed();
        logger.info("Appointment booked succesfully");
        logger.info("Method: " + joinPoint.toString() + " execution ended");
        return result;
    }
    @Around("execution(* com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AppointmentService.cancelAppointment(..))")
    public Object logCancelling(ProceedingJoinPoint joinPoint) throws Throwable {
        logger.info("Method: " + joinPoint.toString() + " execution started");
        Object[] args = joinPoint.getArgs();
        logger.info("Appointment being cancelled for patient " + args[0]);
        Object result = joinPoint.proceed();
        logger.info("Appointment successfully cancelled");
        logger.info("Method: " + joinPoint.toString() + " execution ended");
        return result;
    }
    @Around("execution(* com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PrescriptionService.addPrescription(..))")
    public Object logPrescription(ProceedingJoinPoint joinPoint) throws Throwable{
        logger.info("Method " + joinPoint.toString() + " execution started");
        Object[] args = joinPoint.getArgs();
        logger.info("Prescription being added by doctor: " + args[0]);
        Object result = joinPoint.proceed();
        logger.info("Prescription added successfully");
        logger.info("Method: " + joinPoint.toString() + " execution ended");
        return result;
    }
}
