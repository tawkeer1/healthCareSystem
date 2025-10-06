package com.smartHealthCareAppointmentSystem.HealthCareSystem.appointmentBookingLogic;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepo appointmentRepo;

    @Mock
    private DoctorRepo doctorRepo;

    @Mock
    private PatientRepo patientRepo;

    private Doctor doctor;
    private Patient patient;
    private Appointment existingAppointment;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setAppCount(0L);
        doctor.setSpeciality("Cardiology");

        existingAppointment = new Appointment();
        existingAppointment.setId(10L);
        existingAppointment.setDoctor(doctor);
        existingAppointment.setPatient(patient);
        existingAppointment.setStartTime(LocalDateTime.now().plusHours(1));
        existingAppointment.setEndTime(LocalDateTime.now().plusHours(2));
        existingAppointment.setStatus(Status.BOOKED);
    }

    @Test
    void testBookAppointment_Success() throws Exception {
        LocalDateTime startTime = LocalDateTime.now().plusHours(3);
        LocalDateTime endTime = LocalDateTime.now().plusHours(4);

        when(appointmentRepo.findByDoctorIdAndStatus(doctor.getId(), Status.BOOKED))
                .thenReturn(Collections.emptyList());
        when(appointmentRepo.save(any(Appointment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Appointment booked = appointmentService.bookAppointment(doctor, patient, startTime, endTime);

        assertNotNull(booked);
        assertEquals(doctor, booked.getDoctor());
        assertEquals(patient, booked.getPatient());
        assertEquals(Status.BOOKED, booked.getStatus());
        verify(doctorRepo, times(1)).save(doctor);
        verify(appointmentRepo, times(1)).save(any(Appointment.class));
    }

    @Test
    void testBookAppointment_DoctorBusy() {
        when(appointmentRepo.findByDoctorIdAndStatus(doctor.getId(), Status.BOOKED))
                .thenReturn(List.of(existingAppointment));

        LocalDateTime overlappingStart = existingAppointment.getStartTime().plusMinutes(30);
        LocalDateTime overlappingEnd = existingAppointment.getEndTime().minusMinutes(30);

        assertThrows(DoctorBusyException.class, () ->
                appointmentService.bookAppointment(doctor, patient, overlappingStart, overlappingEnd));
    }


    @Test
    void testBookAppointment_InvalidTime() {
        LocalDateTime startTime = LocalDateTime.now().minusHours(1);
        LocalDateTime endTime = LocalDateTime.now().plusHours(1);

        assertThrows(NotValidTimeException.class, () ->
                appointmentService.bookAppointment(doctor, patient, startTime, endTime));
    }


    @Test
    void testCancelAppointment_Success() throws Exception {
        existingAppointment.setStatus(Status.BOOKED);

        when(appointmentRepo.findById(existingAppointment.getId()))
                .thenReturn(Optional.of(existingAppointment));
        when(appointmentRepo.save(any(Appointment.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        String result = appointmentService.cancelAppointment(existingAppointment.getId(), patient);

        assertEquals("Appointment cancelled successfully", result);
        assertEquals(Status.CANCELLED, existingAppointment.getStatus());
        verify(appointmentRepo, times(1)).save(existingAppointment);
    }


    @Test
    void testCancelAppointment_NotOwned() {
        Patient other = new Patient();
        other.setId(99L);
        existingAppointment.setPatient(other);

        when(appointmentRepo.findById(existingAppointment.getId()))
                .thenReturn(Optional.of(existingAppointment));

        NotValidAppointmentException exception = assertThrows(NotValidAppointmentException.class, () ->
                appointmentService.cancelAppointment(existingAppointment.getId(), patient));

        assertEquals("Please enter your own valid appointment id", exception.getMessage());
    }


    @Test
    void testCancelAppointment_AlreadyCancelled() {
        existingAppointment.setStatus(Status.CANCELLED);
        when(appointmentRepo.findById(existingAppointment.getId()))
                .thenReturn(Optional.of(existingAppointment));

        assertThrows(NotValidAppointmentActionException.class, () ->
                appointmentService.cancelAppointment(existingAppointment.getId(), patient));
    }


    @Test
    void testCancelAppointment_Completed() {
        existingAppointment.setStatus(Status.COMPLETED);
        when(appointmentRepo.findById(existingAppointment.getId()))
                .thenReturn(Optional.of(existingAppointment));

        assertThrows(NotValidAppointmentActionException.class, () ->
                appointmentService.cancelAppointment(existingAppointment.getId(), patient));
    }


    @Test
    void testCancelAppointment_NotFound() {
        when(appointmentRepo.findById(999L)).thenReturn(Optional.empty());

        assertThrows(AppointmentNotFoundException.class, () ->
                appointmentService.cancelAppointment(999L, patient));
    }
}
