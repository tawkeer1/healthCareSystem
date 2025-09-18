package com.smartHealthCareAppointmentSystem.HealthCareSystem.validations;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.annotations.PasswordValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordValidator, String> {
    List<String> weakPasswords;

    @Override
    public void initialize(PasswordValidator passwordValidator){
        weakPasswords = Arrays.asList("12345","password","qwerty");
    }

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.trim().isEmpty()) {
            return false;
        }
        if (weakPasswords.contains(password.toLowerCase())) {
            return false;
        }
        return password.matches(PASSWORD_PATTERN);
    }

}
