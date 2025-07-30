package com.example.users_microservice;

import com.example.users_microservice.config.BrasilApiConfigurationProperties;
import com.exemple.dev_dojo.GlobalExceptionHandlerAdvice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(GlobalExceptionHandlerAdvice.class)
@ConfigurationPropertiesScan
public class UsersMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UsersMicroserviceApplication.class, args);
	}

}
