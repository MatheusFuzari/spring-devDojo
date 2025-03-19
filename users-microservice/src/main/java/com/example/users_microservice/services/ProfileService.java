package com.example.users_microservice.services;

import com.example.users_microservice.domain.Profile;
import com.example.users_microservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository repository;

    public Profile createProfile(Profile profileToCreate) {
        return this.repository.save(profileToCreate);
    }

    public List<Profile> getAllProfiles() {
        return this.repository.findAll();
    }

    public Page<Profile> getAllProfilesPaginated(Pageable page) {
        return this.repository.findAll(page);
    }
}
