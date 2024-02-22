package com.epam.restcontroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AwsRestController {

	@GetMapping("/")
	public String home() {
		return "Welcome to AWS First Deployment...!!";
	}
}
