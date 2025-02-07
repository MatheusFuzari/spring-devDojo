package com.example.users_microservice.services;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.repository.UserHardCodedRepository;
import com.exemple.dev_dojo.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserHardCodedRepository repository;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByName(name);
    }

    public User findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    public User save(User user){
        return repository.save(user);
    }

    public void delete(Long id) {
        var userToDelete = findById(id);
        repository.delete(userToDelete);
    }

    public void update(User user) {
        findById(user.getId());

        repository.update(user);
    }
}
