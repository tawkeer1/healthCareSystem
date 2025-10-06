package com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions;

public class PrescriptionAlreadyAddedException extends Exception{
    public PrescriptionAlreadyAddedException(String message){
        super(message);
    }
}
