package com.example.dev_dojo.config;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfig {
    private final ConnectionConfigurationProperties configurationProperties;

    @Bean
    @Primary
//    @Profile("mysql")
    public Connection connectionMySql() {
        return new Connection(configurationProperties.url(), configurationProperties.username(), configurationProperties.password());
    };

    @Bean
    @Profile("mongo")
    public Connection connectionMongo() {
        return new Connection(configurationProperties.url(), configurationProperties.username(), configurationProperties.password());
    };
}
