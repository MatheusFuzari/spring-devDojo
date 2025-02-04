package com.example.users_microservice.controller;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.repository.UserData;
import com.example.users_microservice.repository.UserHardCodedRepository;
import com.example.users_microservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@WebMvcTest(controllers = UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = "com.example.users_microservice")
@Slf4j
class UserControllerTest {
    private static final String URL = "/v1/users";

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserData userData;

    @MockitoSpyBean
    UserService service;

    @MockitoSpyBean
    UserHardCodedRepository repository;
    @Autowired
    private ResourceLoader resourceLoader;
    private List<User> userList;

    @BeforeEach
    void init() {
        var guts = User.builder().id(1L).firstName("Guts").lastName("").email("guts@fromberserk.com").build();
        var yuji = User.builder().id(2L).firstName("Yuji").lastName("Itadori").email("yuji.itadori@fromjujusu.com").build();
        var kaneki = User.builder().id(3L).firstName("Ken").lastName("Kaneki").email("Ken.Kaneki@fromghoul.com").build();

        this.userList = List.of(guts, yuji, kaneki);
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/users return all users in a list")
    void findAll_ReturnAllUsers_WhenNameIsNull() throws Exception{
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var response = readResourceFile("/users/get-users-null-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));

    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/users?name=Guts returns a user in a list")
    void findAll_ReturnUserInList_WhenNameIsFound() throws Exception{
        var name = userList.getFirst().getFirstName();
        BDDMockito.when(userData.getUsers()).thenReturn(List.of(userList.getFirst()));

        var response = readResourceFile("/users/get-users-guts-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/users?name=X returns a empty list when name is x")
    void findAll_ReturnEmptyList_WhenNameIsX() throws Exception{
        var name = "Yoichi Isagi";
        BDDMockito.when(userData.getUsers()).thenReturn(List.of());

        var response = readResourceFile("/users/get-users-x-name-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL).param("name", name))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }


    public String readResourceFile(String filename) throws IOException {
        var file = resourceLoader.getResource("classpath:%s" .formatted(filename)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }
}