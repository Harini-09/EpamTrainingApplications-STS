package com.epam.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.customexceptions.UserException;
import com.epam.dtos.request.Credential;
import com.epam.service.UserService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/gym/user")
public class UserController {

	@Autowired
	UserService userService;
	
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public void loginUser(@RequestBody @Valid Credential credential) throws UserException {
		log.info("Received GET request to log in a user");
		userService.loginUser(credential);
	} 
	
	@PostMapping("/changepassword/{newPassword}")
	@ResponseStatus(HttpStatus.OK)
	public void changeLogin(@RequestBody @Valid Credential credential,@RequestParam @NotEmpty String newPassword) throws UserException {
		log.info("Received GET request to change password for a user");
		userService.updatePassword(credential,newPassword);
	}

}
