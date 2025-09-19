package com.smartHealthCareAppointmentSystem.HealthCareSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.csrf().disable().authorizeHttpRequests( (requests) -> {
                requests.requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/patient/**").hasRole("PATIENT").
                        requestMatchers("/api/doctor/**").hasRole("DOCTOR")
                        .anyRequest().authenticated();
                }

        ).formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
