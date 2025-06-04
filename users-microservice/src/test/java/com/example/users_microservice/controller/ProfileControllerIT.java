package com.example.users_microservice.controller;

import com.example.users_microservice.common.FileUtils;
import com.example.users_microservice.common.ProfileUtils;
import com.example.users_microservice.config.IntegrationTestConfig;
import com.example.users_microservice.domain.Profile;
import com.example.users_microservice.dto.response.GetProfileResponseDTO;
import net.javacrumbs.jsonunit.assertj.JsonAssertions;
import net.javacrumbs.jsonunit.core.Option;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//Integration test inits tomcat in a random port
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//We also can use instead of a SQL Script to delete all, an annotation called @DirtiesContext and execute before every method.
class ProfileControllerIT extends IntegrationTestConfig {
    private static final String URL = "/v1/profiles";

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProfileUtils profileUtils;
    @Autowired
    private FileUtils fileUtils;

    private static Stream<Arguments> postProfileBadRequestSource() {
        return Stream.of(
                Arguments.of("post-request-profiles-blank-fields-400.json", "post-response-profiles-blank-fields-400.json"),
                Arguments.of("post-request-profiles-empty-fields-400.json", "post-response-profiles-empty-fields-400.json")
        );
    }

    private static HttpEntity<String> buildHttpEntity(String request) {
        var httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(request, httpHeaders);
    }

    @Test
    @DisplayName("GET /v1/profiles returns a list of profiles when successful")
    @Sql(value = "/sql/profiles/init_two_profiles.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) //SQL Injection create profile data to use in the test;
    @Sql(value = "/sql/profiles/clean_profiles.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) //SQL Injection to clean all data after the method;
    @Order(1)
    void findAll_ReturnsAllProfiles_WhenSuccessful() {
        var typeReference = new ParameterizedTypeReference<List<GetProfileResponseDTO>>() {
        };

        ResponseEntity<List<GetProfileResponseDTO>> response = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotEmpty().doesNotContainNull();

        response.getBody().forEach(getProfileResponseDTO -> Assertions.assertThat(getProfileResponseDTO).hasNoNullFieldsOrProperties());
    }

    @Test
    @DisplayName("GET /v1/profiles returns a empty list when nothing is found")
    @Order(2)
    void findAll_ReturnsEmptyList_WhenNothingIsFound() {
        var typeReference = new ParameterizedTypeReference<List<GetProfileResponseDTO>>() {
        };

        ResponseEntity<List<GetProfileResponseDTO>> response = testRestTemplate.exchange(URL, HttpMethod.GET, null, typeReference);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isNotNull().isEmpty();

    }

    @Test
    @DisplayName("POST /v1/profiles creates an profile")
    @Order(3)
    void save_ReturnsCreatedProfile_WhenSuccessful() throws Exception {
        var profileToSave = fileUtils.readResourceFile("/profiles/post-request-profiles-200.json");

        var profileEntity = buildHttpEntity(profileToSave);

        var response = testRestTemplate.exchange(URL, HttpMethod.POST, profileEntity, Profile.class);

        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(response.getBody()).isNotNull().hasNoNullFieldsOrProperties();
    }

    @ParameterizedTest
    @MethodSource("postProfileBadRequestSource")
    @DisplayName("POST /v1/profiles return bad request when fields are invalid")
    @Order(4)
    void save_ReturnsBadRequest_WhenFieldAreInvalid(String requestFile, String responseFile) throws Exception {
        var request = fileUtils.readResourceFile("profiles/%s".formatted(requestFile));
        var expectedResponse = fileUtils.readResourceFile("profiles/%s".formatted(responseFile));

        var profileRequest = buildHttpEntity(request);

        var responseEntity = testRestTemplate.exchange(URL, HttpMethod.POST, profileRequest, String.class);

        Assertions.assertThat(responseEntity).isNotNull();
        Assertions.assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        System.out.println(responseEntity.getBody());

        JsonAssertions.assertThatJson(responseEntity.getBody())
                .whenIgnoringPaths("timestamp")
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(expectedResponse);
    }
}
