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
        var light = User.builder().id(3L).firstName("Light").lastName("Yagami").email("light.yagami@fromdeath.com").build();

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



}