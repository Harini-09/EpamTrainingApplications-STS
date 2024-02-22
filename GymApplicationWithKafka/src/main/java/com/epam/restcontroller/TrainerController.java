package com.epam.restcontroller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.customexceptions.TrainerException;
import com.epam.customexceptions.TrainingTypeException;
import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TrainerDetailsDto;
import com.epam.dtos.request.TrainerProfileUpdateDto;
import com.epam.dtos.response.TrainerBasicDetailsDto;
import com.epam.dtos.response.TrainerDto;
import com.epam.dtos.response.TrainerProfileDto;
import com.epam.dtos.response.TrainerTrainingResponseDto;
import com.epam.service.TrainerService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/gym/trainer")
public class TrainerController {

	@Autowired
	TrainerService trainerService;
	
	@PostMapping("/register")
	public ResponseEntity<TrainerBasicDetailsDto> registerTrainer(@RequestBody @Valid TrainerDetailsDto trainerDetails) throws TrainingTypeException{
		log.info("Received POST request to register a Trainer");
		return new ResponseEntity<>(trainerService.saveTrainer(trainerDetails),HttpStatus.CREATED);
	}

	@GetMapping("/{username}")
	public ResponseEntity<TrainerProfileDto> getTrainerProfile(@PathVariable @NotEmpty String username) throws UserException{
		log.info("Received GET request to retrieve a Trainer Profile");
		return new ResponseEntity<>(trainerService.getTrainer(username),HttpStatus.OK);
	}
	
	@PutMapping
	public ResponseEntity<TrainerProfileDto> updateTrainerProfile(@RequestBody @Valid TrainerProfileUpdateDto trainerProfile) throws UserException{
		log.info("Received PUT request to update a Trainer Profile");
		return new ResponseEntity<>(trainerService.updateTrainer(trainerProfile),HttpStatus.OK);
	}
	
	@GetMapping("/activeTrainer/{username}")
	public ResponseEntity<TrainerDto> getNotAssignedActiveTrainers(@PathVariable @NotEmpty String username) throws UserException, TrainerException{
		log.info("Received GET request to retrieve not assigned on Trainee active trainers");
		return new ResponseEntity<>(trainerService.getNotAssignedActiveTrainers(username),HttpStatus.OK);
	}
	
	@GetMapping("/trainerTrainings")
	public ResponseEntity<List<TrainerTrainingResponseDto>> getTrainerTrainingsList(@RequestParam String username, @RequestParam(required=false) LocalDate periodFrom, @RequestParam(required=false) LocalDate periodTo, @RequestParam(required=false) String traineeName) throws UserException{
		log.info("Received GET request to retrieve a Trainer Trainings List");
		return new ResponseEntity<>(trainerService.getTrainerTrainingsList(username,periodFrom,periodTo,traineeName),HttpStatus.OK);
	}
}
