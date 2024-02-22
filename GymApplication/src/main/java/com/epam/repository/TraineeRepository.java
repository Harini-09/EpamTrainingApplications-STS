package com.epam.repository;

import org.springframework.data.repository.CrudRepository;

import com.epam.entities.Trainee;

public interface TraineeRepository extends CrudRepository<Trainee,Integer>{
	
}
