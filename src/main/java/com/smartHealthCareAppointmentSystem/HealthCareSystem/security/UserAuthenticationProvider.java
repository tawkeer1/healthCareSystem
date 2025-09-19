package com.smartHealthCareAppointmentSystem.HealthCareSystem.security;

import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Patient;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.Role;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.models.User;
import com.smartHealthCareAppointmentSystem.HealthCareSystem.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication){
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userRepo.findByEmail(email);
        if(user == null) throw new BadCredentialsException("User not found");
        String storedPassword = user.getPassword();
//        System.out.println(user.getRole());
        if(!storedPassword.startsWith("$2a$")){
            storedPassword = passwordEncoder.encode(password);
            user.setPassword(storedPassword);
            userRepo.save(user);
        }
        if(passwordEncoder.matches(password,storedPassword)){
            System.out.println(user.getRole());

            return new UsernamePasswordAuthenticationToken(
                    email,
                    password,
                    getGrantedAuthorities(user.getRole())
            );
        }
        else {
            throw new BadCredentialsException("Invalid credentials");
        }
    }
    @Override
    public boolean supports(Class<?> authentication){
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
    private List<GrantedAuthority> getGrantedAuthorities(Role role) {
        System.out.println("In grantedAuthority " + role.name());
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
}
