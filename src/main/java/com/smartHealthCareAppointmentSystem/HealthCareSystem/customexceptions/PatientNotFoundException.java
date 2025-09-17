package com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions;

public class PatientNotFoundException extends Exception{
    public PatientNotFoundException(String message){
        super(message);
    }
}
