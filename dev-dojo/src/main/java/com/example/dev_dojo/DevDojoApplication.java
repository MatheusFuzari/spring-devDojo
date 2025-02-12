package com.example.dev_dojo;

import com.exemple.dev_dojo.GlobalExceptionHandlerAdvice;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import java.util.Arrays;

@SpringBootApplication
@Import(GlobalExceptionHandlerAdvice.class)
//--spring.profiles.active=mongo change profile in argument spring starter
public class DevDojoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevDojoApplication.class, args);
	}

}
