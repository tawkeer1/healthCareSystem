package com.smartHealthCareAppointmentSystem.HealthCareSystem.crud;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.controller.PatientController;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.*;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.AppointmentService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.DoctorService;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@AutoConfigureMockMvc(addFilters = false) //disables security for testing
class PatientControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    @MockBean
    private DoctorService doctorService;

    @MockBean
    private AppointmentService appointmentService;


    @Test
    void testPrescriptionHistory() throws Exception {
        Prescription prescription = new Prescription();
        prescription.setId("1");

        when(patientService.getPrescriptionHistory(any()))
                .thenReturn(List.of(prescription));

        mockMvc.perform(get("/api/patient/prescriptionHistory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"));
    }

    @Test
    void testBookAppointment() throws Exception {
        Appointment appointment = new Appointment();
        appointment.setId(1L);

        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setSpeciality("Cardiology");

        when(doctorService.searchDoctorsBySpeciality("Cardiology")).thenReturn(List.of(doctor));
        when(appointmentService.bookAppointment(any(), any(), any(), any()))
                .thenReturn(appointment);

        mockMvc.perform(post("/api/patient/bookAppointment")
                        .param("speciality", "Cardiology")
                        .param("startTime", "2025-09-23T10:00:00")
                        .param("endTime", "2025-09-23T10:30:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void testCancelAppointment() throws Exception {
        when(appointmentService.cancelAppointment(eq(1L), any()))
                .thenReturn("Appointment cancelled successfully");

        mockMvc.perform(post("/api/patient/cancelAppointment")
                        .param("appointmentId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Appointment cancelled successfully"));
    }
}


