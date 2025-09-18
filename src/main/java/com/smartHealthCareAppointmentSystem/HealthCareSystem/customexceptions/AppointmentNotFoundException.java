package com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions;

public class AppointmentNotFoundException extends Exception{
    public AppointmentNotFoundException(String message){
        super(message);
    }
}
