package com.example.dev_dojo.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
public class ConnectionConfig {
    @Value("${database.url}")
    private String url;
    @Value("${database.username}")
    private String username;
    @Value("${database.password}")
    private String password;

    @Bean
    @Primary
//    @Profile("mysql")
    public Connection connectionMySql() {
        return new Connection(url, username, password);
    };

    @Bean
    @Profile("mongo")
    public Connection connectionMongo() {
        return new Connection(url, username, password);
    };
}
