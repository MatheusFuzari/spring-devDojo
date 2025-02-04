package com.example.users_microservice.controller;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.dto.request.PostUserRequestDTO;
import com.example.users_microservice.dto.request.PutUserRequestDTO;
import com.example.users_microservice.dto.response.GetUserResponseDTO;
import com.example.users_microservice.mapper.UserMapper;
import com.example.users_microservice.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService service;

    private final UserMapper MAPPER = UserMapper.MAPPER;

    @GetMapping
    public ResponseEntity<List<GetUserResponseDTO>> getUsers(@RequestParam(required = false) String name) {
        return ResponseEntity.status(HttpStatus.OK).body(MAPPER.toUserGetResponseList(service.findAll(name)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetUserResponseDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(MAPPER.toUserGetResponse(service.findById(id)));
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody PostUserRequestDTO request){
        var userToSave = MAPPER.toUser(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(userToSave));
    }

    @PutMapping
    public ResponseEntity<Void> updateUser(@RequestBody PutUserRequestDTO request){
        log.info("PUT request: {}", request);

        var userToUpdate = MAPPER.toUser(request);

        service.update(userToUpdate);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        service.delete(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
