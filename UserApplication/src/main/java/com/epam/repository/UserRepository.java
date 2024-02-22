package com.epam.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.epam.entities.User;

public interface UserRepository extends CrudRepository<User,Integer>{
	Optional<User> findByUsername(String username);
}
