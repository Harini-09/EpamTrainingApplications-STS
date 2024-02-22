package com.epam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition
@SpringBootApplication
@EnableFeignClients
public class GymApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymApplication.class, args);
	}

}
