package com.example.users_microservice.common;

import com.example.users_microservice.domain.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileUtils {

    public List<Profile> newProfilesList() {
        var admin = new Profile().withId(1L).withName("Admin").withDescription("Admin Profile");
        var manager = new Profile().withId(2L).withName("Manager").withDescription("Manager Profile");
        var supervisor = new Profile().withId(3L).withName("Supervisor").withDescription("Supervisor Profile");
        var employee = new Profile().withId(4L).withName("Employee").withDescription("Employee Profile");
        return List.of(admin, manager, supervisor, employee);
    }

    public Profile newProfileToSave() {
        return new Profile()
                .withName("Intern")
                .withDescription("Almost a slave");
    }

    public Profile newProfileSaved() {
        return new Profile()
                .withId(99L)
                .withName("Intern")
                .withDescription("Almost a slave");
    }
}
