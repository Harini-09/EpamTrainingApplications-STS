package com.epam.restcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.dtos.UserDto;
import com.epam.service.UserServiceImpl;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value="/users")
public class UserRestController {

	@Autowired
	UserServiceImpl userService;
	
	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<UserDto>> getUsers(){
		log.info("Received GET request to retrieve all the users");
		return new ResponseEntity<>(userService.viewUsers(),HttpStatus.OK);
	}
	
	@GetMapping(value="/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDto> getUserByUsername(@PathVariable("username") String username) {
		log.info("Received GET request to get a user with user name - {}",username);
		return new ResponseEntity<>(userService.getUserByUsername(username),HttpStatus.OK);
	}
	
	@PostMapping(produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDto> save(@RequestBody @Valid UserDto userDto){
		log.info("Received POST request to save a user with user details - {}",userDto);
		return new ResponseEntity<>(userService.addUser(userDto),HttpStatus.CREATED);
	}
	
	@DeleteMapping(value="/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("username") String username) {
		log.info("Received DELETE request to delete a user with user name - {}",username);
		userService.deleteByUsername(username);
	}
	
	@PutMapping(value="/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDto> updateUser(@PathVariable("username") String username,@RequestBody @Valid UserDto userDto) {
		log.info("Received PUT request to update a user with user name - {}, with the updated details - {}",username,userDto);
		return new ResponseEntity<>(userService.update(username, userDto),HttpStatus.OK);
	}
}
