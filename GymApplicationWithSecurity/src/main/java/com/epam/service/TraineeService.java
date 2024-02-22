package com.epam.service;

import java.time.LocalDate;
import java.util.List;

import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TraineeDetailsDto;
import com.epam.dtos.request.TraineeProfileUpdate;
import com.epam.dtos.response.TraineeBasicDetailsDto;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeTrainingResponseDto;
import com.epam.dtos.response.TrainerDto;

public interface TraineeService {

	public TraineeBasicDetailsDto saveTrainee(TraineeDetailsDto traineeDetails);
	public TraineeProfileDto getTrainee(String username) throws UserException;
	public TraineeProfileDto updateTrainee(TraineeProfileUpdate traineeProfile) throws UserException;
	public void deleteTrainee(String username) throws UserException;
	public List<TrainerDto> updateTraineeTrainerList(String traineeUserName,List<String> trainerUserNames) throws UserException;
	public List<TraineeTrainingResponseDto> getTraineeTrainingsList(String username, LocalDate periodFrom, LocalDate periodTo, String trainerName,
			String trainingType) throws UserException;
}
