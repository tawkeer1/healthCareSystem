package com.smartHealthCareAppointmentSystem.HealthCareSystem.appointmentBookingLogic;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.DoctorBusyException;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.AppointmentRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.DoctorRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.PatientRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.UserRepo;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepo appointmentRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private PatientRepo patientRepo;

    @Mock
    private DoctorRepo doctorRepo;

    @Mock
    private Authentication authentication;

    private User user;
    private Patient patient;
    private Doctor doctor;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("patient@example.com");

        patient = new Patient();
        patient.setId(1L);
        patient.setUser(user);

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUser(new User());
        doctor.setSpeciality("Cardiology");

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setStartTime(LocalDateTime.now().plusHours(1));
        appointment.setEndTime(LocalDateTime.now().plusHours(2));
        appointment.setStatus(Status.BOOKED);
    }

    @Test
    void testBookAppointment_Success() throws Exception {
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepo.findByEmail(user.getEmail())).thenReturn(user);
        when(patientRepo.findByUserId(user.getId())).thenReturn(patient);
        when(appointmentRepo.findByDoctorIdAndStatus(doctor.getId(), Status.BOOKED)).thenReturn(java.util.Collections.emptyList());
        when(appointmentRepo.save(any(Appointment.class))).thenReturn(appointment);

        Appointment booked = appointmentService.bookAppointment(doctor, appointment.getStartTime(), appointment.getEndTime(), authentication);

        assertNotNull(booked);
        assertEquals(doctor, booked.getDoctor());
        assertEquals(patient, booked.getPatient());
        verify(appointmentRepo, times(1)).save(any(Appointment.class));
    }

    @Test
    void testBookAppointment_DoctorBusy() throws Exception {
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepo.findByEmail(user.getEmail())).thenReturn(user);
        when(patientRepo.findByUserId(user.getId())).thenReturn(patient);
        when(appointmentRepo.findByDoctorIdAndStatus(doctor.getId(), Status.BOOKED)).thenReturn(java.util.List.of(appointment));

        assertThrows(DoctorBusyException.class,
                () -> appointmentService.bookAppointment(doctor, appointment.getStartTime(), appointment.getEndTime(), authentication));
    }

//    @Test
//    void testCancelAppointment_Success() throws Exception {
//
//        when(authentication.getName()).thenReturn(user.getEmail());
//        when(userRepo.findByEmail(user.getEmail())).thenReturn(user);
//        when(patientRepo.findByUserId(user.getId())).thenReturn(patient);
//        appointment.setPatient(patient);
//        when(appointmentRepo.findById(appointment.getId())).thenReturn(Optional.of(appointment));
//        when(appointmentRepo.save(any(Appointment.class))).thenReturn(appointment);
//
//
//        String result = appointmentService.cancelAppointment(appointment.getId(), authentication);
//
//        assertEquals("Appointment cancelled successfully", result);
//        assertEquals(Status.CANCELLED, appointment.getStatus());
//        verify(appointmentRepo, times(1)).save(appointment);
//    }

    @Test
    void testCancelAppointment_NotOwned() {
        when(authentication.getName()).thenReturn(user.getEmail());
        when(userRepo.findByEmail(user.getEmail())).thenReturn(user);
        when(patientRepo.findByUserId(user.getId())).thenReturn(patient);
        when(appointmentRepo.findById(appointment.getId())).thenReturn(Optional.of(appointment));

        // appointment belongs to another patient
        appointment.getPatient().setId(99L);

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> appointmentService.cancelAppointment(appointment.getId(), authentication));
        assertEquals("Please enter your own valid appointment id", exception.getMessage());
    }
}

