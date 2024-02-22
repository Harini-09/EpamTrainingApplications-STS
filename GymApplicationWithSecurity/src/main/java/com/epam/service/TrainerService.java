package com.epam.service;

import java.time.LocalDate;
import java.util.List;

import com.epam.customexceptions.TrainerException;
import com.epam.customexceptions.TrainingTypeException;
import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TrainerDetailsDto;
import com.epam.dtos.request.TrainerProfileUpdateDto;
import com.epam.dtos.response.TrainerBasicDetailsDto;
import com.epam.dtos.response.TrainerDto;
import com.epam.dtos.response.TrainerProfileDto;
import com.epam.dtos.response.TrainerTrainingResponseDto;

public interface TrainerService {

	public TrainerBasicDetailsDto saveTrainer(TrainerDetailsDto trainerDetails) throws TrainingTypeException;
	public TrainerProfileDto getTrainer(String username) throws UserException;
	public TrainerProfileDto updateTrainer(TrainerProfileUpdateDto trainerProfile) throws UserException;
	public TrainerDto getNotAssignedActiveTrainers(String username) throws UserException, TrainerException;
	public List<TrainerTrainingResponseDto> getTrainerTrainingsList(String username, LocalDate periodFrom, LocalDate periodTo,
			String traineeName) throws UserException;
}
