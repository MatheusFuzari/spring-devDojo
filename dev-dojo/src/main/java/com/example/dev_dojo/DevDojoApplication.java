package com.example.dev_dojo;

import com.example.dev_dojo.config.ConnectionConfigurationProperties;
import com.exemple.dev_dojo.GlobalExceptionHandlerAdvice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties(ConnectionConfigurationProperties.class)
@Import(GlobalExceptionHandlerAdvice.class)
//@ComponentScan(basePackages = {"outside", "com"} )
//--spring.profiles.active=mongo change profile in argument spring starter
public class DevDojoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevDojoApplication.class, args);
	}

}
