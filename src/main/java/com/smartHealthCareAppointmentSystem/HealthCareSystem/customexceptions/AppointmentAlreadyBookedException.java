package com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions;

public class AppointmentAlreadyBookedException extends Exception{
    public AppointmentAlreadyBookedException(String message){
        super(message);
    }
}
