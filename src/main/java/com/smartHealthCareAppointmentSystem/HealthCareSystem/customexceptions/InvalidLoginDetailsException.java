package com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions;

public class InvalidLoginDetailsException extends Exception{
    public InvalidLoginDetailsException(String message){
        super(message);
    }
}
