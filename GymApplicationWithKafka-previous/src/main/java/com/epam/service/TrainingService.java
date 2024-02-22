package com.epam.service;

import com.epam.customexceptions.TrainerException;
import com.epam.customexceptions.TrainingTypeException;
import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TrainingDetailsDto;

public interface TrainingService {

	public void saveTraining(TrainingDetailsDto trainingDetails) throws UserException, TrainingTypeException, TrainerException;
}
