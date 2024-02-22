package com.epam;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AssessmentDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssessmentDemoApplication.class, args);
	}
	
//	@Bean
//	public ModelMapper getModelMapper() {
//		return new ModelMapper();
//	}

}
