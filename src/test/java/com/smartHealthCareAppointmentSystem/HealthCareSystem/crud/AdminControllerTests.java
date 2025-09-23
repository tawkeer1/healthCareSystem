package com.smartHealthCareAppointmentSystem.HealthCareSystem.crud;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.controller.AdminController;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AdminService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false) //disables security for testing
class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private PatientService patientService;

    @MockBean
    private AdminService adminService;

    private Doctor buildDoctor() {
        User user = new User();
        user.setId(10L);
        user.setName("Dr. Test");
        user.setEmail("Test@hospital.com");
        user.setPassword("diagnose123");
        user.setRole(Role.DOCTOR);

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUser(user);
        doctor.setSpeciality("Diagnostics");
        doctor.setLicenseNumber("123");
        return doctor;
    }

    private Patient buildPatient() {
        User user = new User();
        user.setId(20L);
        user.setName("Mr. Ali");
        user.setEmail("ali@gmail.com");
        user.setPassword("AliAli@123");
        user.setRole(Role.PATIENT);

        Patient patient = new Patient();
        patient.setId(2L);
        patient.setUser(user);
        patient.setAddress("Srinagar JK");
        patient.setMedicalHistory("Knee injury");
        patient.setFamilyHistory("None");
        return patient;
    }

    @Test
    void testCreateDoctor() throws Exception {
        Doctor doctor = buildDoctor();
        when(doctorService.createDoctor(any(Doctor.class))).thenReturn(doctor);

        mockMvc.perform(post("/api/admin/createDoctor")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.speciality").value("Diagnostics"))
                .andExpect(jsonPath("$.licenseNumber").value("123"));

        verify(doctorService, times(1)).createDoctor(any(Doctor.class));
    }

    @Test
    void testDeleteDoctor() throws Exception {
        when(doctorService.deleteDoctor(1L)).thenReturn("Doctor deleted successfully");

        mockMvc.perform(delete("/api/admin/deleteDoctor/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Doctor deleted successfully"));

        verify(doctorService, times(1)).deleteDoctor(1L);
    }

    @Test
    void testCreatePatient() throws Exception {
        Patient patient = buildPatient();
        when(patientService.createPatient(any(Patient.class))).thenReturn(patient);

        mockMvc.perform(post("/api/admin/createPatient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Srinagar JK"))
                .andExpect(jsonPath("$.medicalHistory").value("Knee injury"));

        verify(patientService, times(1)).createPatient(any(Patient.class));
    }

    @Test
    void testDeletePatient() throws Exception {
        when(patientService.deletePatient(2L)).thenReturn("Patient deleted successfully");

        mockMvc.perform(delete("/api/admin/deletePatient/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("Patient deleted successfully"));

        verify(patientService, times(1)).deletePatient(2L);
    }

    @Test
    void testGetAllDoctors() throws Exception {
        Doctor doctor = buildDoctor();
        when(adminService.getAllDoctors(0)).thenReturn(Collections.singletonList(doctor));

        mockMvc.perform(get("/api/admin/allDoctors/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].speciality").value("Diagnostics"));

        verify(adminService, times(1)).getAllDoctors(0);
    }

    @Test
    void testGetAllPatients() throws Exception {
        Patient patient = buildPatient();
        when(adminService.getAllPatients(0)).thenReturn(Collections.singletonList(patient));

        mockMvc.perform(get("/api/admin/allPatients/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("Srinagar JK"));

        verify(adminService, times(1)).getAllPatients(0);
    }

}
