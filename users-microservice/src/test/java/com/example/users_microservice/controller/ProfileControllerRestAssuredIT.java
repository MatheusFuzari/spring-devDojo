package com.example.users_microservice.controller;

import com.example.users_microservice.common.FileUtils;
import com.example.users_microservice.config.IntegrationTestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
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

import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//Integration test inits tomcat in a random port
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileControllerRestAssuredIT extends IntegrationTestConfig {
    private static final String URL = "/v1/profiles";

    @Autowired
    private FileUtils fileUtils;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUrl() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    @DisplayName("GET /v1/profiles returns a list of profiles when successful")
    @Sql(value = "/sql/profiles/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //SQL Injection create profile data to use in the test;
    @Sql(value = "/sql/profiles/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) //SQL Injection to clean all data after the method;
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() {
        var response = fileUtils.readResourceFile("/profiles/get-all-profiles-200.json");

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
    @DisplayName("GET /v1/profiles returns a empty list when nothing is found")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenNothingIsFound() {
        var response = fileUtils.readResourceFile("/profiles/get-profiles-empty-list-200.json");

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
    @DisplayName("POST /v1/profiles creates an profile")
    @Order(3)
    void save_ReturnsCreatedProfile_WhenSuccessful() throws Exception {
        var request = fileUtils.readResourceFile("/profiles/post-request-profiles-200.json");
        var expected_response = fileUtils.readResourceFile("/profiles/post-response-profiles-201.json");

        var response = RestAssured
                .given()
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

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST /v1/profiles return bad request when fields are invalid")
    @Order(4)
    void save_ReturnsBadRequest_WhenFieldAreInvalid(String requestFile, String responseFile) throws Exception {
        var request = fileUtils.readResourceFile("profiles/%s".formatted(requestFile));
        var expected_response = fileUtils.readResourceFile("profiles/%s".formatted(responseFile));

        var response = RestAssured
                .given()
                    .contentType(ContentType.JSON).accept(ContentType.JSON)
                    .body(request)
                .when()
                    .post(URL)
                .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                .log().all()
                .extract().response().body().asString();

        JsonAssertions.assertThatJson(response)
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expected_response);
    }

    private static Stream<Arguments> postProfileBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-profiles-blank-fields-400.json", "post-response-profiles-blank-fields-400.json"),
                Arguments.of("post-request-profiles-empty-fields-400.json", "post-response-profiles-empty-fields-400.json")
        );
    }
}
