package com.example.users_microservice.controller;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.domain.UserProfile;
import com.example.users_microservice.dto.request.PostUserRequestDTO;
import com.example.users_microservice.dto.request.PutUserRequestDTO;
import com.example.users_microservice.dto.response.GetUserResponseDTO;
import com.example.users_microservice.mapper.UserMapper;
import com.example.users_microservice.repository.UserProfileRepository;
import com.example.users_microservice.services.UserProfileService;
import com.example.users_microservice.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user-profiles")
@RequiredArgsConstructor
@Slf4j
public class UserProfileController {

    private final UserProfileService service;

    @GetMapping
    public ResponseEntity<List<UserProfile>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(service.findAll());
    }

}
