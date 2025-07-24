package com.example.users_microservice.controller;

import com.example.users_microservice.common.FileUtils;
import com.example.users_microservice.config.IntegrationTestConfig;
import com.example.users_microservice.config.RestAssuredConfig;
import com.example.users_microservice.repository.UserRepository;
import com.exemple.dev_dojo.NotFoundException;
import groovy.util.logging.Slf4j;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

@lombok.extern.slf4j.Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = RestAssuredConfig.class)
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier(value = "requestSpecificationRegularUser")
    private RequestSpecification requestSpecificationRegularUser;

    @Autowired
    @Qualifier(value = "requestSpecificationAdminUser")
    private RequestSpecification requestSpecificationAdminUser;

    @BeforeEach
    void init() {
        RestAssured.requestSpecification = requestSpecificationRegularUser;
    }

    @Test
    @Order(1)
    @DisplayName("GET /v1/users return all users in a list")
    @Sql(value = "/sql/users/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnAllUsers_WhenNameIsNull() {
        RestAssured.requestSpecification = requestSpecificationAdminUser;

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
    @Sql(value = "/sql/users/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnUserInList_WhenNameIsFound() {
        RestAssured.requestSpecification = requestSpecificationAdminUser;

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
    @Sql(value = "/sql/users/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAll_ReturnEmptyList_WhenNameIsX() {
        RestAssured.requestSpecification = requestSpecificationAdminUser;

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
    @Sql(value = "/sql/users/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
    @Sql(value = "/sql/users/init_three_users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
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
                .whenIgnoringPaths("id", "password")
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
    @Sql(value = "/sql/users/init_one_login_admin_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_DeleteUser_WhenIdExists() {
        RestAssured.requestSpecification = requestSpecificationAdminUser;

        var id = repository.findAll().getFirst().getId();

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
    @Sql(value = "/sql/users/init_one_login_admin_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_ThrowsResponseStatusException_WhenIdIsNotFound() {
        RestAssured.requestSpecification = requestSpecificationAdminUser;

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
        var request = fileUtils.readResourceFile("/users/put-request-users-200.json");
        var userToUpdate = repository.findByFirstNameIgnoreCase("Guts");

        var oldUser = userToUpdate.getFirst();

        Assertions.assertThat(userToUpdate).hasSize(1);


        request = request.replace("1", userToUpdate.getFirst().getId().toString());

        RestAssured.given()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                    .body(request)
                .when()
                    .put(URL)
                .then()
                    .statusCode(HttpStatus.NO_CONTENT.value())
                .log().all();

        var updatedUser = repository.findById(oldUser.getId()).orElseThrow(() -> new NotFoundException("User not Found"));
        var encryptedPassword = updatedUser.getPassword();
        Assertions.assertThat(passwordEncoder.matches("casca", encryptedPassword)).isTrue();
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
    void save_ReturnBadRequest_WhenFieldsEmpty(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("/users/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("/users/%s".formatted(responseFile));

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
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);

    }

    @ParameterizedTest
    @MethodSource("putUserBadRequestSource")
    @Order(14)
    @DisplayName("PUT /v1/users returns bad request when field are empty")
    @Sql(value = "/sql/users/init_one_login_regular_user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(value = "/sql/users/clean_users.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void update_ReturnBadRequest_WhenFieldsEmpty(String requestFile, String responseFile) {
        var request = fileUtils.readResourceFile("/users/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("/users/%s".formatted(responseFile));

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
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);

    }

    private static Stream<Arguments> postUserBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-users-empty-fields-400.json", "post-response-users-empty-fields-400.json"),
                Arguments.of("post-request-users-blank-fields-400.json", "post-response-users-blank-fields-400.json"),
                Arguments.of("post-request-users-invalid-email-400.json", "post-response-users-invalid-email-400.json")
        );
    }

    private static Stream<Arguments> putUserBadRequestSource() {
        return Stream.of(
                Arguments.of("put-request-users-empty-fields-400.json", "put-response-users-empty-fields-400.json"),
                Arguments.of("put-request-users-blank-fields-400.json", "put-response-users-blank-fields-400.json"),
                Arguments.of("put-request-users-invalid-email-400.json", "put-response-users-invalid-email-400.json"),
                Arguments.of("put-request-users-null-id-400.json", "put-response-users-null-id-400.json")
        );
    }


}