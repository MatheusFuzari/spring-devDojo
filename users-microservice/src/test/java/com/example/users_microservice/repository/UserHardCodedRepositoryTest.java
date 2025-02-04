package com.example.users_microservice.repository;

import com.example.users_microservice.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class UserHardCodedRepositoryTest {

    @InjectMocks
    private UserHardCodedRepository repository;
    @Mock
    private UserData userData;

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
    @DisplayName("findAll returns a list of users when successful")
    void findAll_ReturnAllUsers_WhenSuccessful(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var users = repository.findAll();

        Assertions.assertThat(users).isNotEmpty().hasSameElementsAs(userList);
    }

    @Test
    @Order(2)
    @DisplayName("findByName returns a user in a list when name is found")
    void findAll_ReturnUserInList_WhenNameIsFound() {
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var userToFind = userList.getFirst();
        var userFound = repository.findByName(userToFind.getFirstName());

        Assertions.assertThat(userFound).hasSize(1).contains(userList.getFirst());
    }

    @Test
    @Order(3)
    @DisplayName("findAll returns a empty list when name is not found")
    void findAll_ReturnEmptyList_WhenNameIsNotFound(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var user = repository.findByName("Yoichi Isagi");

        Assertions.assertThat(user).isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("findById return user when id is found")
    void findById_ReturnUser_WhenIdIsFound(){
        BDDMockito.when(userData.getUsers()).thenReturn(userList);
        var userToFind = userList.getFirst();
        var userFound = repository.findById(userToFind.getId());

        Assertions.assertThat(userFound).isPresent().get().isEqualTo(userList.getFirst());
    }

    @Test
    @Order(5)
    @DisplayName("save creates an user when successful")
    void save_CreatesUser_WhenSuccessful(){
        var userToSave = User.builder().id(99L).firstName("Yoichi").lastName("Isagi").email("yoichi.isagi@fromblue.com").build();
        var userCreated = repository.save(userToSave);

        Assertions.assertThat(userCreated).hasNoNullFieldsOrProperties().isEqualTo(userToSave);
    }

    @Test
    @Order(6)
    @DisplayName("delete deletes an user when successful")
    void delete_DeleteUser_WhenSuccessful(){
        var userToDelete = userList.getFirst();

        repository.delete(userToDelete);

        Assertions.assertThat(repository.findAll()).doesNotContain(userToDelete);
    }

    @Test
    @Order(7)
    @DisplayName("update updates an user when successful")
    void update_UpdateUser_WhenSuccessful(){
        var userToUpdate = userList.getFirst();
        userToUpdate.setEmail("monkey.d.luffy@fromOnePiece.com");

        repository.update(userToUpdate);

        Assertions.assertThat(userList).isNotEmpty().contains(userToUpdate);
    }



}