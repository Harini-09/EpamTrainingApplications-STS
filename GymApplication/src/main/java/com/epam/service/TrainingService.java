package com.epam.service;

import com.epam.customexceptions.TrainerException;
import com.epam.customexceptions.TrainingTypeException;
import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TrainingDetailsDto;
import com.epam.dtos.response.TrainingReportDto;

public interface TrainingService {

	public TrainingReportDto saveTraining(TrainingDetailsDto trainingDetails) throws UserException, TrainingTypeException, TrainerException;
}
