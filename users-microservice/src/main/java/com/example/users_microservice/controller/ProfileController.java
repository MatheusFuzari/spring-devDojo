package com.example.users_microservice.controller;

import com.example.users_microservice.domain.Profile;
import com.example.users_microservice.dto.request.PostProfileRequestDTO;
import com.example.users_microservice.dto.response.GetProfileResponseDTO;
import com.example.users_microservice.mapper.ProfileMapper;
import com.example.users_microservice.services.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/profiles")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

    private final ProfileService service;

    private final ProfileMapper MAPPER = ProfileMapper.MAPPER;

    @GetMapping()
    private ResponseEntity<List<GetProfileResponseDTO>> getAllProfiles() {
        var profiles = MAPPER.toGetResponseList(service.getAllProfiles());

        return ResponseEntity.status(HttpStatus.OK).body(profiles);
    }

    @GetMapping("/paginated")
    private ResponseEntity<Page<Profile>> getAllProfilesPaginated(Pageable page) {
        var profilesPaginated = service.getAllProfilesPaginated(page);

        return ResponseEntity.status(HttpStatus.OK).body(profilesPaginated);
    }

    @PostMapping()
    private ResponseEntity<Profile> createProfile(@RequestBody @Valid PostProfileRequestDTO newProfile){
        var profileToCreate = MAPPER.toProfile(newProfile);
        var createdProfile = service.createProfile(profileToCreate);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdProfile);
    }
}
