package com.epam.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.epam.entities.TrainingType;

public interface TrainingTypeRepository extends CrudRepository<TrainingType,Integer>{

	public Optional<TrainingType> findByTrainingTypeName(String trainingTypeName);
}
