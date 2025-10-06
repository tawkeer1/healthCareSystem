package com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions;

public class NotValidAppointmentActionException extends Exception{
    public NotValidAppointmentActionException(String message){
        super(message);
    }
}
