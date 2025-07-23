package com.example.users_microservice.services;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.mapper.UserMapper;
import com.example.users_microservice.repository.UserRepository;
import com.exemple.dev_dojo.EmailAlreadyExistsException;
import com.exemple.dev_dojo.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    public List<User> findAll(String name) {
        return name == null ? repository.findAll() : repository.findByFirstNameIgnoreCase(name);
    }

    public User findByIdOrThrowNotFound(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
    }

//    @Transactional
    public User save(User user){
        assertEmailDoesNotExist(user.getEmail());
        return repository.save(user);
    }

    public void delete(Long id) {
        var userToDelete = findByIdOrThrowNotFound(id);
        repository.delete(userToDelete);
    }

    public void update(User user) {
        User savedUser = findByIdOrThrowNotFound(user.getId());
        assertEmailDoesNotExist(user.getEmail(), user.getId());

        User userToSave = mapper.toUserWithPasswordAndRoles(user, user.getPassword(), savedUser);

        repository.save(userToSave);
    }

    public void assertEmailDoesNotExist(String email){
        repository.findByEmail(email).ifPresent(this::throwEmailExistsException);
    }

    public void assertEmailDoesNotExist(String email, Long id){
        repository.findByEmailAndIdNot(email, id).ifPresent(this::throwEmailExistsException);
    }

    private void throwEmailExistsException(User user) {
        throw new EmailAlreadyExistsException("Email %s already registered".formatted(user.getEmail()));
    }
}
