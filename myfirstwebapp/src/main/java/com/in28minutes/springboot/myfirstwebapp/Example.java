package com.in28minutes.springboot.myfirstwebapp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class Example {
	
	@RequestMapping("/")
	@ResponseBody
	public String sayHello() {
		System.out.println("entered method!!");
		return "Hello,How are you?";
		
	}
	
	@RequestMapping("/say-hello")	
	public String home() {
		return "sayHello";
	}

}
