package com.example.users_microservice.config;

import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
@Lazy // Indicates to Spring to first initialize every container and tomcat server, and then load the configuration below
public class TestRestTemplateConfig {
    @LocalServerPort
    int port;

    @Bean
    public TestRestTemplate testRestTemplate() {
        var uri = new DefaultUriBuilderFactory("http://localhost:" + port);
        var testRestTemplate = new TestRestTemplate()
                .withBasicAuth("satoru.gojo@fromjujutsu.com", "test");
        testRestTemplate.setUriTemplateHandler(uri);
        return testRestTemplate;
    }

}
