package com.epam.proxy;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.epam.dtos.UserDto;

import jakarta.validation.Valid;

@FeignClient(name="user",url="http://localhost:9000/users")
public interface UserProxy {
	
	@GetMapping(produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<List<UserDto>> getUsers();
	
	@GetMapping(value="/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDto> getUserByUsername(@PathVariable("username") String username);
	
	@PostMapping(produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDto> save(@RequestBody @Valid UserDto userDto);
	
	@DeleteMapping(value="/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	@ResponseStatus(value=HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("username") String username);
	
	@PutMapping(value="/{username}",produces= {MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_XML_VALUE })
	public ResponseEntity<UserDto> updateUser(@PathVariable("username") String username,@RequestBody @Valid UserDto userDto);
}
