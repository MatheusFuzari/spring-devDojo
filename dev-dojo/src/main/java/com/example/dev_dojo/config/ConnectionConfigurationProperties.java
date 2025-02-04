package com.example.dev_dojo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "database")
public record ConnectionConfigurationProperties(String url, String username, String password) {}
