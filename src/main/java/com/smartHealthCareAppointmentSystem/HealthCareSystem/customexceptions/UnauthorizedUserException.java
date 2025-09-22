package com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions;

public class UnauthorizedUserException extends Exception{
    public UnauthorizedUserException(String message){
        super(message);
    }
}
