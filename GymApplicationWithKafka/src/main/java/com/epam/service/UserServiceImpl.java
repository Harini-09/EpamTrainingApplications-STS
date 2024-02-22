package com.epam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.customexceptions.UserException;
import com.epam.dtos.request.Credential;
import com.epam.entities.User;
import com.epam.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService{
	 
	@Autowired
	UserRepository userRepo;
	
	@Override
	public void loginUser(Credential credential) throws UserException{
		log.info("Entered into the User Service - loginUser() method to log in a user");
		userRepo.findByUserNameAndPassword(credential.getUsername(),credential.getPassword())
		.orElseThrow(()->new UserException("Invalid Credentials!!"));
		
	}

	@Override
	@Transactional
	public void updatePassword(Credential credential, String newPassword) throws UserException {
		log.info("Entered into the User Service - updatePassword() method to update a password of a user");
		User user = userRepo.findByUserNameAndPassword(credential.getUsername(),credential.getPassword())
		.orElseThrow(()->new UserException("Invalid Credentials!!"));
		user.setPassword(newPassword);
		userRepo.save(user);
	}



}
