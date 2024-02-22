package com.epam.restcontroller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TraineeDetailsDto;
import com.epam.dtos.request.TraineeProfileUpdate;
import com.epam.dtos.response.TraineeBasicDetailsDto;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeTrainingResponseDto;
import com.epam.dtos.response.TrainerDto;
import com.epam.service.TraineeService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/gym/trainee")
public class TraineeController {

	@Autowired
	TraineeService traineeService;

	@PostMapping("/register")
	public ResponseEntity<TraineeBasicDetailsDto> registerTrainee(@RequestBody @Valid TraineeDetailsDto traineeDetails) {
		log.info("Received POST request to register a Trainee");
		return new ResponseEntity<>(traineeService.saveTrainee(traineeDetails), HttpStatus.CREATED);
	}

	@GetMapping("/{username}")
	public ResponseEntity<TraineeProfileDto> getTraineeProfile(@PathVariable("username") @NotEmpty String username) throws UserException {
		log.info("Received GET request to retrieve a Trainee Profile");
		return new ResponseEntity<>(traineeService.getTrainee(username), HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<TraineeProfileDto> updateTraineeProfile(@RequestBody @Valid TraineeProfileUpdate traineeProfile) throws UserException {
		log.info("Received PUT request to update a Trainee Profile");
		return new ResponseEntity<>(traineeService.updateTrainee(traineeProfile), HttpStatus.OK);
	}

	@DeleteMapping("/{username}")
	@ResponseStatus(HttpStatus.OK)
	public void deleteTraineeProfile(@PathVariable("username") @NotEmpty String username) throws UserException {
		log.info("Received DELETE request to delete a Trainee Profile");
		traineeService.deleteTrainee(username);
	}

	@PutMapping("/{username}/{trainerUserNames}")
	public ResponseEntity<List<TrainerDto>> updateTraineeTrainerList(
			@PathVariable("username") @NotEmpty String traineeUserName,
			@PathVariable @Valid List<String> trainerUserNames) throws UserException {
		log.info("Received PUT request to update a Trainee Trainers List");
		return new ResponseEntity<>(traineeService.updateTraineeTrainerList(traineeUserName, trainerUserNames),HttpStatus.OK);
	}
 
	@GetMapping("/traineeTrainings")
	public ResponseEntity<List<TraineeTrainingResponseDto>> getTraineeTrainingsList(
			@RequestParam String username,
			@RequestParam(required = false) LocalDate periodFrom, 
			@RequestParam(required = false) LocalDate periodTo,
			@RequestParam(required = false) String trainerName, 
			@RequestParam(required = false) String trainingType) throws UserException {
		log.info("Received GET request to retrieve a Trainee Trainings List");
		return new ResponseEntity<>(traineeService.getTraineeTrainingsList(username, periodFrom, periodTo, trainerName, trainingType),
				HttpStatus.OK);
	}

}
