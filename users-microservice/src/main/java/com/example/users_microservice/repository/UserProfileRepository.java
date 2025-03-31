package com.example.users_microservice.repository;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.domain.UserProfile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    @Query("SELECT up FROM UserProfile up join fetch up.user u join fetch up.profile p")
    List<UserProfile> retriveAll();

//  @EntityGraph(attributePaths = {"user", "profile"})
    @EntityGraph(value = "UserProfile.fullDetails")
    List<UserProfile> findAll();

    @Query("SELECT up.user FROM UserProfile up WHERE up.profile.id = ?1")
    List<User> findAllUsersByProfileId(Long id);
}
