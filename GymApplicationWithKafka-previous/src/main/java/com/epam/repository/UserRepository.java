package com.epam.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.epam.entities.User;

public interface UserRepository extends CrudRepository<User,Integer>{

	public Optional<User> findByUserNameAndPassword(String userName,String password);
	public Optional<User> findByUserName(String userName);
	
}
