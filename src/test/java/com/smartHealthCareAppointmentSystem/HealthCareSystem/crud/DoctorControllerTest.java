package com.smartHealthCareAppointmentSystem.HealthCareSystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
        import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    private Doctor buildDoctor() {
        User user = new User();
        user.setId(10L);
        user.setName("Dr. Strange");
        user.setEmail("strange@marvel.com");
        user.setPassword("securePass123");
        user.setRole(Role.DOCTOR);

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setUser(user);
        doctor.setSpeciality("Neurology");
        doctor.setLicenseNumber("LIC12345");
        return doctor;
    }

    @Test
    void testMarkAppointmentAsCompleted() throws Exception {
        when(doctorService.markAppointmentAsCompleted(eq(1L), any()))
                .thenReturn("Appointment marked as completed");

        mockMvc.perform(patch("/api/doctor/markAppointmentAsCompleted")
                        .param("appointmentId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment marked as completed"));

        verify(doctorService, times(1))
                .markAppointmentAsCompleted(eq(1L), any());
    }

    @Test
    void testAddPrescription() throws Exception {
        Medication medication = new Medication();
        medication.setMedicines(List.of("Paracetamol"));
        medication.setDosage("Take one tablet twice daily");
        LabReport labReport = new LabReport();
        labReport.setType("Blood Test");
        labReport.setNote("All good");
        labReport.setResult("Fine");

        medication.setLabReports(List.of(labReport));

        Prescription prescription = new Prescription();
        prescription.setId("1");

        when(doctorService.addPrescription(eq(1L), any(Medication.class), any()))
                .thenReturn(prescription);

        mockMvc.perform(post("/api/doctor/addPrescription")
                        .param("appointmentId", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(medication)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));

        verify(doctorService, times(1))
                .addPrescription(eq(1L), any(Medication.class), any());
    }

    @Test
    void testGetTodayAppointments() throws Exception {
        Appointment appointment = new Appointment();
        appointment.setId(1L);

        when(doctorService.getTodayAppointments(any()))
                .thenReturn(Collections.singletonList(appointment));

        mockMvc.perform(get("/api/doctor/getTodayAppointments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(doctorService, times(1)).getTodayAppointments(any());
    }

    @WithMockUser(username = "doctor", roles = {"DOCTOR"})
    @Test
    void testUpdatePersonalDetails() throws Exception {
        Doctor doctor = buildDoctor();

        when(doctorService.updatePersonalDetails(any(), any()))
                .thenReturn(doctor);

        mockMvc.perform(patch("/api/doctor/updatePersonalDetails")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"speciality\":\"Neurology\",\"licenseNumber\":\"LIC12345\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.speciality").value("Neurology"))
                .andExpect(jsonPath("$.licenseNumber").value("LIC12345"));

        verify(doctorService, times(1))
                .updatePersonalDetails(any(), any());
    }
}





