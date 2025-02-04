package com.example.users_microservice.services;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.repository.UserHardCodedRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {

    @InjectMocks
    UserService service;
    @Mock
    UserHardCodedRepository repository;

    private List<User> userList;

    @BeforeEach
    void init() {
        var luffy = User.builder().id(1L).firstName("Monkey").lastName("D. Luffy").email("monkey.d.luffy@fromOnePiece.com").build();
        var naruto = User.builder().id(2L).firstName("Naruto").lastName("Uzumaki").email("naruto.uzumaki@fromnaruto.com").build();
        var saitama = User.builder().id(3L).firstName("Saitama").lastName("").email("saitama@frompunch.com").build();
        var light = User.builder().id(4L).firstName("Light").lastName("Yagami").email("light.yagami@fromdeath.com").build();

        this.userList = List.of(luffy, naruto, saitama, light);
    }

    @Test
    @Order(1)
    @DisplayName("findAll returns a list of users when name is null")
    void findAll_ReturnAllUsers_WhenNameIsNull(){
        BDDMockito.when(repository.findAll()).thenReturn(userList);

        var users = service.findAll(null);

        Assertions.assertThat(users).isNotEmpty().hasSameElementsAs(userList);
    }

    @Test
    @Order(2)
    @DisplayName("findByName returns a user in a list when name is found (Monkey D.Luffy)")
    void findAll_ReturnUserInList_WhenNameIsFound() {
        var userToFind = userList.getFirst();

        BDDMockito.when(repository.findByName(userToFind.getFirstName())).thenReturn(List.of(userList.getFirst()));

        var userFound = service.findAll(userToFind.getFirstName());

        Assertions.assertThat(userFound).hasSize(1).contains(userList.getFirst());
    }

    @Test
    @Order(3)
    @DisplayName("findAll returns a empty list when name is x")
    void findAll_ReturnEmptyList_WhenNameIsX(){
        BDDMockito.when(repository.findByName("Eren Yegar")).thenReturn(List.of());

        var user = service.findAll("Eren Yegar");

        Assertions.assertThat(user).isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findById return user when id is 1")
    void findById_ReturnUser_WhenIdIsFound(){
        var userToFind = userList.getFirst();
        BDDMockito.when(repository.findById(userToFind.getId())).thenReturn(Optional.of(userToFind));
        var userFound = repository.findById(userToFind.getId());

        Assertions.assertThat(userFound).isPresent().get().isEqualTo(userToFind);
    }

    @Test
    @Order(5)
    @DisplayName("findById return user when id is 99")
    void findById_ThrowsResponseStatusException_WhenIdIsNotFound(){
        BDDMockito.when(repository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.findById(99L))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(6)
    @DisplayName("save creates an user when successful")
    void save_CreatesUser_WhenSuccessful(){
        var userToSave = User.builder().id(99L).firstName("Yoichi").lastName("Isagi").email("yoichi.isagi@fromblue.com").build();
        BDDMockito.when(service.save(userToSave)).thenReturn(userToSave);
        var userCreated = service.save(userToSave);

        Assertions.assertThat(userCreated).hasNoNullFieldsOrProperties().isEqualTo(userToSave);
    }

    @Test
    @Order(7)
    @DisplayName("delete deletes an user id exists")
    void delete_DeleteUser_WhenIdExists(){
        var userToDelete = userList.getFirst();
        BDDMockito.when(repository.findById(userToDelete.getId())).thenReturn(Optional.of(userToDelete));
        BDDMockito.doNothing().when(repository).delete(userToDelete);

        Assertions.assertThatNoException().isThrownBy(() -> service.delete(userToDelete.getId()));
    }

    @Test
    @Order(8)
    @DisplayName("delete throws ResponseStatusException when id is not found")
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound(){
        var id = 99L;
        BDDMockito.when(repository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThatException()
                .isThrownBy(() -> service.delete(id))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    @Order(9)
    @DisplayName("update updates an user when id exists")
    void update_UpdateUser_WhenIdExists(){
        var userToUpdate = userList.getFirst();
        userToUpdate.setEmail("monkey.d.luffy@fromOnePiece.com");

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));
        BDDMockito.doNothing().when(repository).update(userToUpdate);

        repository.update(userToUpdate);

        Assertions.assertThatNoException()
                .isThrownBy(() -> service.update(userToUpdate));
    }

    @Test
    @Order(10)
    @DisplayName("update throws ResponseStatusException an user when id is not found")
    void update_ThrowsResponseStatusException_WhenIdIsNotFound(){
        var userToUpdate = userList.getFirst();
        userToUpdate.setEmail("monkey.d.luffy@fromOnePiece.com");

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.empty());
        BDDMockito.doNothing().when(repository).update(userToUpdate);

        repository.update(userToUpdate);

        Assertions.assertThatException()
                .isThrownBy(() -> service.update(userToUpdate))
                .isInstanceOf(ResponseStatusException.class);
    }

}