package com.assistance.DogShelter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(scanBasePackages = "com.assistance.DogShelter")
@OpenAPIDefinition
public class DogShelterApplication {
	public static void main(String[] args) {
		SpringApplication.run(DogShelterApplication.class, args);
	}

}
