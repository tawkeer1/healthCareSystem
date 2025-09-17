package com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions;

public class DoctorNotFoundException extends Exception{
    public DoctorNotFoundException(String message){
        super(message);
    }
}
