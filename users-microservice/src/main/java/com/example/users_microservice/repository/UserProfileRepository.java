package com.example.users_microservice.repository;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

}
