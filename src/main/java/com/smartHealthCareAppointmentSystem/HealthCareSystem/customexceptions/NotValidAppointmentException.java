package com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions;

public class NotValidAppointmentException extends Exception{
    public NotValidAppointmentException(String message){
        super(message);
    }
}
