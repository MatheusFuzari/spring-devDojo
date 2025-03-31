package com.example.users_microservice.services;

import com.example.users_microservice.domain.Profile;
import com.example.users_microservice.domain.User;
import com.example.users_microservice.domain.UserProfile;
import com.example.users_microservice.repository.ProfileRepository;
import com.example.users_microservice.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository repository;

    public List<UserProfile> findAll() {
        return this.repository.findAll();
    }

    public List<User> findAllUsersByProfileId(Long id) {
        return this.repository.findAllUsersByProfileId(id);
    }
}
