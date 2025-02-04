package com.example.users_microservice.repository;

import com.example.users_microservice.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
@Slf4j
public class UserHardCodedRepository {

    private final UserData users;

    public List<User> findAll() {
        return users.getUsers();
    }

    public List<User> findByName(String name) {
        return users.getUsers().stream().filter(user -> user.getFullName().contains(name)).toList();
    }

    public Optional<User> findById(Long id) {
        return users.getUsers().stream().filter(user -> user.getId().equals(id)).findFirst();
    }

    public User save(User user){
        users.getUsers().add(user);
        return user;
    }

    public void delete(User user) {
        users.getUsers().remove(user);
    }

    public void update(User user) {
        delete(user);
        var updatedUser = save(user);
    }
}
