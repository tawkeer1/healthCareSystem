package com.smartHealthCareAppointmentSystem.HealthCareSystem.crud;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.controller.AdminController;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.customexceptions.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AdminService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestCRUDOperations {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private DoctorService doctorService;

    @Mock
    private PatientService patientService;

    @Mock
    private AdminService adminService;

    @Mock
    private Authentication authentication;

    private Doctor doctor;
    private DoctorRequest doctorRequest;
    private Patient patient;
    private PatientRequest patientRequest;

    @BeforeEach
    void setUp() {
        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setSpeciality("Cardiology");

        doctorRequest = new DoctorRequest();
        doctorRequest.setSpeciality("Neurology");

        patient = new Patient();
        patient.setId(1L);
        patient.setMedicalHistory("None");

        patientRequest = new PatientRequest();
        patientRequest.setMedicalHistory("Allergic to penicillin");
    }

    // ---------- Doctor CRUD ----------

    @Test
    void testCreateDoctor() throws DoctorNotFoundException, UserAlreadyExistsException {
        when(doctorService.createDoctor(any(Doctor.class))).thenReturn(doctor);

        Doctor result = adminController.createDoctor(doctor);

        assertNotNull(result);
        assertEquals(doctor.getId(), result.getId());
        verify(doctorService, times(1)).createDoctor(doctor);
    }

    @Test
    void testCreateDoctorForUser() throws UserNotFoundException, DoctorNotFoundException {
        when(doctorService.createDoctorIfUserExists(any(DoctorRequest.class), eq(1L))).thenReturn(doctor);

        Doctor result = adminController.createDoctorForUser(1L, doctorRequest);

        assertNotNull(result);
        assertEquals(doctor.getId(), result.getId());
        verify(doctorService, times(1)).createDoctorIfUserExists(doctorRequest, 1L);
    }

    @Test
    void testUpdateDoctor() throws UnauthorizedUserException, DoctorNotFoundException {
        when(doctorService.updateDoctor(eq(1L), any(DoctorRequest.class), eq(authentication))).thenReturn(doctor);

        Doctor result = adminController.updateDoctor(1L, doctorRequest, authentication);

        assertNotNull(result);
        verify(doctorService, times(1)).updateDoctor(1L, doctorRequest, authentication);
    }

    @Test
    void testDeleteDoctor() throws DoctorNotFoundException {
        when(doctorService.deleteDoctor(1L)).thenReturn("Doctor deleted successfully");

        String result = adminController.deleteDoctor(1L);

        assertEquals("Doctor deleted successfully", result);
        verify(doctorService, times(1)).deleteDoctor(1L);
    }

    // ---------- Patient CRUD ----------

    @Test
    void testCreatePatient() throws PatientNotFoundException,UserAlreadyExistsException {
        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        Patient result = adminController.createPatient(patient);

        assertNotNull(result);
        assertEquals(patient.getId(), result.getId());
        verify(patientService, times(1)).createPatient(patient);
    }

    @Test
    void testCreatePatientForUser() throws UserNotFoundException {
        when(patientService.createPatientIfUserExists(any(PatientRequest.class), eq(1L))).thenReturn(patient);

        Patient result = adminController.createPaitentForUser(1L, patientRequest);

        assertNotNull(result);
        assertEquals(patient.getId(), result.getId());
        verify(patientService, times(1)).createPatientIfUserExists(patientRequest, 1L);
    }

    @Test
    void testUpdatePatient() throws PatientNotFoundException, UnauthorizedUserException {
        when(patientService.updatePatient(eq(1L), any(PatientRequest.class), eq(authentication))).thenReturn(patient);

        Patient result = adminController.updatePatient(1L, patientRequest, authentication);

        assertNotNull(result);
        verify(patientService, times(1)).updatePatient(1L, patientRequest, authentication);
    }

    @Test
    void testDeletePatient() throws PatientNotFoundException {
        when(patientService.deletePatient(1L)).thenReturn("Patient deleted successfully");

        String result = adminController.deletePatient(1L);

        assertEquals("Patient deleted successfully", result);
        verify(patientService, times(1)).deletePatient(1L);
    }

    // ---------- AdminService List Retrieval ----------

    @Test
    void testGetAllDoctors() {
        when(adminService.getAllDoctors(0)).thenReturn(List.of(doctor));

        List<Doctor> result = adminController.getAllDoctors(0);

        assertEquals(1, result.size());
        verify(adminService, times(1)).getAllDoctors(0);
    }

    @Test
    void testGetAllPatients() {
        when(adminService.getAllPatients(0)).thenReturn(List.of(patient));

        List<Patient> result = adminController.getAllPatients(0);

        assertEquals(1, result.size());
        verify(adminService, times(1)).getAllPatients(0);
    }
}

