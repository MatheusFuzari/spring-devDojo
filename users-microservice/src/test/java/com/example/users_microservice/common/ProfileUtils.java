package com.example.users_microservice.common;

import com.example.users_microservice.domain.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileUtils {

    public List<Profile> newProfiles() {
        var admin = new Profile().withId(1L).withName("Admin").withDescription("Admin Profile");
        var supervisor = new Profile().withId(2L).withName("Supervisor").withDescription("Supervisor Profile");
        var employee = new Profile().withId(3L).withName("Employee").withDescription("Employee Profile");
        return List.of(admin, supervisor, employee);
    }
}
