package com.smartHealthCareAppointmentSystem.HealthCareSystem.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = CascadeType.ALL, targetEntity = Patient.class)
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Patient patient;
    @NotNull
    @Size(min = 5, max = 100, message = "Note should be between 5 and 100 characters")
    private String note;
    private List<String> medicines;
    @NotNull
    @Size(min = 5, max = 100, message = "Note should be between 5 and 100 characters")
    private String labResult;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<String> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<String> medicines) {
        this.medicines = medicines;
    }

    public String getLabResult() {
        return labResult;
    }

    public void setLabResult(String labResult) {
        this.labResult = labResult;
    }
}
