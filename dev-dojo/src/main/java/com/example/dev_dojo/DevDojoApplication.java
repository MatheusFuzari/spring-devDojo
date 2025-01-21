package com.example.dev_dojo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
//@ComponentScan(basePackages = {"outside", "com"} )
public class DevDojoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevDojoApplication.class, args);
	}

}
