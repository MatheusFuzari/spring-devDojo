package com.example.users_microservice.config;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import static com.example.users_microservice.common.Constants.*;

@TestConfiguration
@Lazy
public class RestAssuredConfig {

    @LocalServerPort
    int port;

    @Bean(name = "requestSpecificationRegularUser")
    public RequestSpecification requestSpecificationRegularUser() {
        return RestAssured.given()
                .baseUri(BASE_URI + port)
                // .preemptive() tell restAssured to login in before doing the request
                //if we don't use the restAssured will make the request, receive an 401 then try to log-in and try again
                .auth().basic(REGULAR_USERNAME, PASSWORD);
    }

    @Bean(name = "requestSpecificationAdminUser")
    public RequestSpecification requestSpecificationAdminUser() {
        return RestAssured.given()
                .baseUri(BASE_URI + port)
                //preemptive tell restAssured to login in before doing the request
                //if we don't use the restAssured will make the request, receive an 401 then try to log-in and try again
                .auth().basic(ADMIN_USERNAME, PASSWORD);
    }
}
