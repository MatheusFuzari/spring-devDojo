package com.example.dev_dojo.config;


import external.dependecy.Connection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ConnectionConfig {

    @Bean
    @Primary
    public Connection connectionMySql() {
        return new Connection("localhost","fum4ca","123");
    };

    @Bean
    public Connection connectionMongo() {
        return new Connection("localhost","fum4ca","123");
    };
}
