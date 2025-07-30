package com.example.users_microservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "brasil-api")
public record BrasilApiConfigurationProperties(String baseUrl, String cepUri) {}

// RestTemplate (antigo), WebClient (reativo, para usar c/ WebFlux), RestClient