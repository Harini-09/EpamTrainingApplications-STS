package com.epam.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.customexceptions.TrainerException;
import com.epam.customexceptions.TrainingTypeException;
import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TrainingDetailsDto;
import com.epam.service.TrainingService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/gym/training")
public class TrainingController {

	@Autowired
	TrainingService trainingService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.OK)
	public void saveTraining(@RequestBody @Valid TrainingDetailsDto trainingDetails) throws UserException, TrainingTypeException, TrainerException {
		log.info("Received POST request to save a Training");
		trainingService.saveTraining(trainingDetails);
	}

} 
 