package com.example.users_microservice.controller;

import com.example.users_microservice.common.FileUtils;
import com.example.users_microservice.config.IntegrationTestConfig;
import com.example.users_microservice.repository.UserRepository;
import groovy.util.logging.Slf4j;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@lombok.extern.slf4j.Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class UserControllerRestAssuredIT extends IntegrationTestConfig {
    private static final String URL = "/v1/users";

    @LocalServerPort
    private int port;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserRepository repository;

    @BeforeEach
    void init() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/users return all users in a list")
    @Sql(value = "/sql/users/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnAllUsers_WhenNameIsNull() {
        var response = fileUtils.readResourceFile("/users/get-users-null-name-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                    .get(URL)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(Matchers.equalTo(response))
                .log().all();

    }

    @Test
    @Order(2)
    @DisplayName("GET /v1/users?name=Guts returns a user in a list")
    @Sql(value = "/sql/users/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnUserInList_WhenNameIsFound() {
        var name = "Guts";
        var response = fileUtils.readResourceFile("/users/get-users-guts-name-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .param("name", name)
                .when()
                    .get(URL)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @Order(3)
    @DisplayName("GET /v1/users?name=X returns a empty list when name is x")
    void findAll_ReturnEmptyList_WhenNameIsX() {
        var name = "Yoichi";
        var response = fileUtils.readResourceFile("/users/get-users-x-name-200.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .param("name", name)
                .when()
                    .get(URL)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @Order(4)
    @DisplayName("GET /v1/users/1 findById return an user")
    @Sql(value = "/sql/users/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ReturnUser_WhenIdIsFound() throws Exception {
        var user = repository.findByFirstNameIgnoreCase("Guts");
        var id = user.getFirst().getId();
        var expected_response = fileUtils.readResourceFile("/users/get-users-by-id-200.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                    .get(URL+"/{id}", id)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .body(Matchers.equalTo(expected_response))
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .node("id")
                .asNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expected_response);
    }

    @Test
    @Order(5)
    @DisplayName("GET /v1/users/99 findById throws an ResponseStatusException")
    @Sql(value = "/sql/users/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findById_ThrowsResponseStatusException_WhenIdIsNotFound() {
        var id = 99L;
        var response = fileUtils.readResourceFile("/users/get-users-by-id-404.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                    .get(URL+"/{id}", id)
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @Order(6)
    @DisplayName("POST /v1/users creates an user when successful")
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_CreatesUser_WhenSuccessful(){
        var request = fileUtils.readResourceFile("/users/post-request-users-200.json");
        var expected_response = fileUtils.readResourceFile("/users/post-response-users-201.json");

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                    .body(request)
                .when()
                    .post(URL)
                .then()
                    .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .node("id")
                .asNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("id")
                .isEqualTo(expected_response);
    }

    @Test
    @Order(7)
    @DisplayName("POST /v1/users throws ResponseStatusException when email already exists")
    @Sql(value = "/sql/users/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void save_ThrowsResponseStatusException_WhenEmailAlreadyExists() {
        var request = fileUtils.readResourceFile("/users/post-request-users-200.json");
        var response = fileUtils.readResourceFile("/users/post-response-users-400.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .body(request)
                .when()
                    .post(URL)
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @Order(8)
    @DisplayName("DELETE /v1/users/1 deletes an user with id exists")
    @Sql(value = "/sql/users/init_one_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_DeleteUser_WhenIdExists() {
        var user = repository.findByFirstNameIgnoreCase("Guts");
        var id = user.getFirst().getId();

        RestAssured.given()
            .contentType(ContentType.JSON).accept(ContentType.JSON)
            .when()
                .delete(URL+"/{id}", id)
            .then()
                .statusCode(HttpStatus.NO_CONTENT.value())
            .log().all();
    }

    @Test
    @Order(9)
    @DisplayName("DELETE /v1/users/99 throws ResponseStatusException")
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() {
        var userToDelete = 99L;

        var response = fileUtils.readResourceFile("/users/get-users-by-id-404.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .when()
                    .delete(URL+"/{id}", userToDelete)
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @Order(10)
    @DisplayName("PUT /v1/users updates an user when id exists")
    @Sql(value = "/sql/users/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_UpdateUser_WhenIdExists() {
        var userToUpdate = repository.findByFirstNameIgnoreCase("Guts");

        Assertions.assertThat(userToUpdate).hasSize(1);

        var request = fileUtils.readResourceFile("/users/put-request-users-200.json");

        request = request.replace("1", userToUpdate.getFirst().getId().toString());

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                    .body(request)
                .when()
                    .put(URL)
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

    }

    @Test
    @Order(11)
    @DisplayName("PUT /v1/users throws ResponseStatusException an user when id is not found")
    @Sql(value = "/sql/users/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_ThrowsResponseStatusException_WhenIdIsNotFound() {

        var request = fileUtils.readResourceFile("/users/put-request-users-404.json");
        var response = fileUtils.readResourceFile("/users/get-users-by-id-404.json");

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                    .body(request)
                .when()
                    .put(URL)
                .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body(Matchers.equalTo(response))
                .log().all();
    }

    @Test
    @Order(12)
    @DisplayName("PUT /v1/users throws ResponseStatusException when email already exists")
    @Sql(value = "/sql/users/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_ThrowsResponseStatusException_WhenEmailAlreadyExists() {
        var userToUpdate = repository.findByFirstNameIgnoreCase("Yuji");

        var request = fileUtils.readResourceFile("/users/put-request-users-400.json");
        var expected_response = fileUtils.readResourceFile("/users/put-response-users-400.json");

        request = request.replace("1", userToUpdate.getFirst().getId().toString());

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                    .body(request)
                .when()
                    .put(URL)
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body(Matchers.equalTo(expected_response))
                .log().all();
    }

    @ParameterizedTest
    @MethodSource("postUserBadRequestSource")
    @Order(13)
    @DisplayName("POST /v1/users return bad request when fields are empty")
    void save_ReturnBadRequest_WhenFieldsEmpty(String filename, List<String> errors) {
        var request = fileUtils.readResourceFile("/users/%s".formatted(filename));

        var response = RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                    .body(request)
                .when()
                    .post(URL)
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .node("status")
                .asNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .when(Option.IGNORING_ARRAY_ORDER)
                .node("message")
                .isEqualTo(errors);

    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @Order(14)
    @DisplayName("PUT /v1/users returns bad request when field are empty")
    void update_ReturnBadRequest_WhenFieldsEmpty(String filename, List<String> errors) {
        var request = fileUtils.readResourceFile("/users/%s".formatted(filename));

        var response = RestAssured.given()
                        .contentType(ContentType.JSON).accept(ContentType.JSON)
                            .body(request)
                        .when()
                            .put(URL)
                        .then()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                        .log().all()
                        .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .node("status")
                .asNumber()
                .isPositive();

        JsonAssertions.assertThatJson(response)
                .when(Option.IGNORING_ARRAY_ORDER)
                .node("message")
                .isEqualTo(errors);

    }

    public static List<String> invalidEmail() {
        var emailInvalidError = "'email' is not valid";
        return Collections.singletonList(emailInvalidError);
    }

    public static List<String> allRequiredErrors() {
        var firstNameError = "The field 'firstName' is required";
        var lastNameError = "The field 'lastName' is required";
        var emailRequiredError = "The field 'email' is required";
        var emailInvalidError = "'email' is not valid";
        return new ArrayList<>(List.of(firstNameError, lastNameError, emailRequiredError, emailInvalidError));
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


}