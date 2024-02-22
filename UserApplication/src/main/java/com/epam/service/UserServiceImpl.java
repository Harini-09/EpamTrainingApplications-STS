package com.epam.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.epam.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

import com.epam.dtos.UserDto;
import com.epam.entities.User;

@Slf4j
@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepo;

	@Autowired
	ModelMapper modelMapper;
	 
	String message = "User doesn't exist with this Username : ";

	@Override
	public List<UserDto> viewUsers() {
		log.info("Entered into the User Service - viewUsers() method to get all the Users from the library");
		return ((List<User>) userRepo.findAll()).stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
	}

	@Override
	public UserDto getUserByUsername(String username) {
		log.info("Entered into the User Service - getUserByUsername() method to get a user with the username - {}",
				username);
		return userRepo.findByUsername(username).map(user->{
		log.info("User viewed successfully!!");
		return UserDto.builder()
				.username(user.getUsername())
				.name(user.getName())
				.email(user.getEmail()).build();
		}).orElseGet(()->{
			log.info(message+username);
			return UserDto.builder()
				.timeStamp(new Date().toString())
				.developerMessage(message+username)
				.httpStatus(HttpStatus.OK).build();
		});
	} 

	@Override
	public UserDto addUser(UserDto userDto) {
		log.info("Entered into the User Service - save() method to save a user - {}", userDto);
		User user = modelMapper.map(userDto, User.class);
		userRepo.save(user);
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public void deleteByUsername(String username) {
		log.info("Entered into the User Service - deleteByUsername() method to delete a user with the username - {}",
				username);
		Optional<User> user = userRepo.findByUsername(username);
		if (user.isPresent()) {
			userRepo.delete(user.get());
		}
	}

	@Override
	public UserDto update(String username, UserDto userDto){
		log.info(
				"Entered into the User Service - update() method to update a user with the user name : {} with the updated details - {}",
				username, userDto);
		return userRepo.findByUsername(username).map(user->{
			user.setUsername(userDto.getUsername());
			user.setName(userDto.getName());
			user.setEmail(userDto.getEmail());
			return modelMapper.map(user, UserDto.class);
		}).orElseGet(()->{
			log.info(message+username);
			return UserDto.builder()
					.timeStamp(new Date().toString())
					.developerMessage(message+username)
					.httpStatus(HttpStatus.OK).build();
		});
	} 

}
