package com.smartHealthCareAppointmentSystem.HealthCareSystem.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;

import java.util.List;

public class Medication {
    @NotNull
    List<String> medicines;
    @NotNull
    @Size(min = 3, max = 100, message = "Dosage length cannot be less than 3 or greater than 100 characters")
    String dosage;

    @NotNull
    List<LabReport> labReports;
    public List<String> getMedicines() {
        return medicines;
    }

    public void setMedicines(List<String> medicines) {
        this.medicines = medicines;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public List<LabReport> getLabReports() {
        return labReports;
    }

    public void setLabReports(List<LabReport> labReports) {
        this.labReports = labReports;
    }
}
