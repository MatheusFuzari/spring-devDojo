package com.example.users_microservice.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.util.DefaultUriBuilderFactory;

import static com.example.users_microservice.common.Constants.*;

@TestConfiguration
@Lazy // Indicates to Spring to first initialize every container and tomcat server, and then load the configuration below
public class TestRestTemplateConfig {
    @LocalServerPort
    int port;

    @Bean
    public TestRestTemplate testRestTemplate() {
        // When override the testRestTemplate instance, you need to give the URI on the build method, because it don't have it by default
        var uri = new DefaultUriBuilderFactory(BASE_URI + port);
        var testRestTemplate = new TestRestTemplate()
                .withBasicAuth(REGULAR_USERNAME, PASSWORD);
        testRestTemplate.setUriTemplateHandler(uri);
        return testRestTemplate;
    }

}
