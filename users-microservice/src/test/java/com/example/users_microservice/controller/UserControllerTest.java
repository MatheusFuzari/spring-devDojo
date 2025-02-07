package com.example.users_microservice.controller;

import com.example.users_microservice.domain.User;
import com.example.users_microservice.repository.UserData;
import com.example.users_microservice.repository.UserHardCodedRepository;
import com.example.users_microservice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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

    @Test
    @Order(4)
    @DisplayName("GET /v1/users/1 findById return an user")
    void findById_ReturnUser_WhenIdIsFound() throws Exception{
        var id = userList.getFirst().getId();
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var response = readResourceFile("/users/get-users-by-id-200.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(5)
    @DisplayName("GET /v1/users/99 findById throws an ResponseStatusException")
    void findById_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception{
        var id = 99L;
        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var response = readResourceFile("/users/get-users-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.get(URL+"/{id}", id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(6)
    @DisplayName("POST /v1/users creates an user when successful")
    void save_CreatesUser_WhenSuccessful() throws Exception{
        var userToSave = User.builder().id(99L).firstName("Yoichi").lastName("Isagi").email("yoichi.isagi@fromblue.com").build();

        var request = readResourceFile("/users/post-request-users-200.json");
        var response = readResourceFile("/users/post-response-users-201.json");

        BDDMockito.when(service.save(ArgumentMatchers.any())).thenReturn(userToSave);

        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(7)
    @DisplayName("DELETE /v1/users/1 deletes an user with id exists")
    void delete_DeleteUser_WhenIdExists() throws Exception{
        var id = userList.getFirst().getId();

        BDDMockito.when(repository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(userList.getFirst()));

        mockMvc.perform(MockMvcRequestBuilders.delete(URL+"/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());

    }

    @Test
    @Order(8)
    @DisplayName("DELETE /v1/users/99 throws ResponseStatusException")
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception{
        var id = 99L;

        BDDMockito.when(userData.getUsers()).thenReturn(userList);

        var response = readResourceFile("/users/get-users-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.delete(URL+"/{id}",id))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @Test
    @Order(9)
    @DisplayName("PUT /v1/users updates an user when id exists")
    void update_UpdateUser_WhenIdExists() throws Exception{
        var userToUpdate = userList.getFirst();

        var request = readResourceFile("/users/put-request-users-200.json");

        BDDMockito.when(repository.findById(userToUpdate.getId())).thenReturn(Optional.of(userToUpdate));

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @Order(10)
    @DisplayName("PUT /v1/users throws ResponseStatusException an user when id is not found")
    void update_ThrowsResponseStatusException_WhenIdIsNotFound() throws Exception{
        var userToUpdate = userList.getFirst();

        var request = readResourceFile("/users/put-request-users-404.json");
        var response = readResourceFile("/users/get-users-by-id-404.json");

        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(response));
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @Order(11)
    @DisplayName("POST /v1/users return bad request when fields are empty")
    void save_ReturnBadRequest_WhenFieldsEmpty(String filename, List<String> errors) throws Exception{
        var request = readResourceFile("/users/%s".formatted(filename));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .content(request)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var responseError = mvcResult.getResolvedException();

        Assertions.assertThat(responseError).isNotNull();

        Assertions.assertThat(responseError.getMessage())
                .contains(errors);
    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @Order(12)
    @DisplayName("PUT /v1/users returns bad request when field are empty")
    void update_ReturnBadRequest_WhenFieldsEmpty(String filename, List<String> errors) throws Exception{

        var request = readResourceFile("/users/%s".formatted(filename));

        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(request))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        var responseError = mvcResult.getResolvedException();

        Assertions.assertThat(responseError).isNotNull();

        Assertions.assertThat(responseError.getMessage()).contains(errors);
    }


    public String readResourceFile(String filename) throws IOException {
        var file = resourceLoader.getResource("classpath:%s" .formatted(filename)).getFile();
        return new String(Files.readAllBytes(file.toPath()));
    }

    private static Stream<Arguments> postUserBadRequestSource() {
        var allErrors = allRequiredErrors();
        var emailErrors = invalidEmail();

        return Stream.of(
            Arguments.of("post-request-users-empty-fields-400.json", allErrors),
            Arguments.of("post-request-users-blank-fields-400.json", allErrors),
            Arguments.of("post-request-users-invalid-email-400.json", emailErrors)
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        var idNullError = "The field 'id' cannot be null";
        var allErrors = allRequiredErrors();
        var emailErrors = invalidEmail();
        var idErrors = Collections.singletonList(idNullError);

        allErrors.add(idNullError);
        return Stream.of(
                Arguments.of("put-request-users-empty-fields-400.json", allErrors),
                Arguments.of("put-request-users-blank-fields-400.json", allErrors),
                Arguments.of("put-request-users-invalid-email-400.json", emailErrors),
                Arguments.of("put-request-users-null-id-400.json", idErrors)
        );
    }

    public static List<String> invalidEmail(){
        var emailInvalidError = "'email' is not valid";
        return Collections.singletonList(emailInvalidError);
    }

    public static List<String> allRequiredErrors(){
        var firstNameError = "The field 'firstName' is required";
        var lastNameError = "The field 'lastName' is required";
        var emailRequiredError = "The field 'email' is required";
        return new ArrayList<>(List.of(firstNameError, lastNameError, emailRequiredError));
    }
}