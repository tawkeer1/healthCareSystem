package com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions;

public class DoctorBusyException extends Exception{
    public DoctorBusyException(String message){
        super(message);
    }
}
