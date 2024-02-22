package com.epam.service;

import com.epam.entities.TrainingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.repository.TrainingTypeRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainingTypeServiceImpl implements TrainingTypeService{

	@Autowired
	TrainingTypeRepository trainingTypeRepo;
	
	@Override
	public void saveTrainingType(String trainingTypeName) {
		log.info("Entered into the TrainingType Service - saveTrainingType() method to save a TrainingType into the library");
		TrainingType trainingType = new TrainingType();
		trainingType.setTrainingTypeName(trainingTypeName);
		trainingTypeRepo.save(trainingType);
	}
 
	 

} 
