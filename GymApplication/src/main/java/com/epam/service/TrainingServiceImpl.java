package com.epam.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.customexceptions.TrainerException;
import com.epam.customexceptions.TrainingTypeException;
import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TrainingDetailsDto;
import com.epam.dtos.response.NotificationDto;
import com.epam.dtos.response.TrainingReportDto;
import com.epam.entities.Training;
import com.epam.entities.TrainingType;
import com.epam.entities.User;
import com.epam.proxy.NotificationProxy;
import com.epam.repository.TrainingRepository;
import com.epam.repository.TrainingTypeRepository;
import com.epam.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainingServiceImpl implements TrainingService{

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	TrainingTypeRepository trainingTypeRepo;
	
	@Autowired
	TrainingRepository trainingRepo;
	
	@Autowired
	NotificationProxy notificationProxy;
	
	@Override
	public TrainingReportDto saveTraining(TrainingDetailsDto trainingDetails) throws UserException, TrainingTypeException, TrainerException {
		log.info("Entered into the Training Service - saveTraining() method to save a Training into the library");
		Training training = new Training();
		User userTrainee = userRepo.findByUserName(trainingDetails.getTraineeUserName()).orElseThrow(()->new UserException("Invalid Username for Trainee!!"));		
	 
	 userTrainee.getTrainee().getTrainersList()
				.stream()
				.filter(trainer->trainer.getUser().getUserName().equals(trainingDetails.getTrainerUserName()))
				.findAny()
				.orElseThrow(()->new TrainerException("The entered Trainer is not allocated to this particular Trainee"));
		training.setTrainee(userTrainee.getTrainee());
		User userTrainer = userRepo.findByUserName(trainingDetails.getTrainerUserName()).orElseThrow(()->new UserException("Invalid Username for Trainer!!"));
		if(!userTrainer.getTrainer().getTrainingType().getTrainingTypeName().equals(trainingDetails.getTrainingTypeName())){
			throw new TrainerException("The entered Training Type is not allocated to this particular trainer");
		}				
		training.setTrainer(userTrainer.getTrainer());
		training.setTrainingName(trainingDetails.getTrainingName());
		training.setTrainingDate(trainingDetails.getTrainingDate());
		TrainingType trainingType = trainingTypeRepo.findByTrainingTypeName(trainingDetails.getTrainingTypeName()).orElseThrow(()->new TrainingTypeException("Invalid Training Type!!"));
		training.setTrainingType(trainingType);
		training.setTrainingDuration(trainingDetails.getTrainingDuration());
		
		TrainingReportDto trainingReportDto = TrainingReportDto.builder()
														 .trainerUserName(trainingDetails.getTrainerUserName())
														 .trainerFirstName(userTrainer.getFirstName())
														 .trainerLastName(userTrainer.getLastName())
														 .isTrainerActive(userTrainer.isActive())
														 .trainerEmail(userTrainer.getEmail())
														 .date(trainingDetails.getTrainingDate())
														 .duration(trainingDetails.getTrainingDuration())
														 .build();
		trainingRepo.save(training);
		
		NotificationDto notificationDto=NotificationDto.builder().subject("Training is added Successfully").toEmails(List.of(userTrainee.getEmail(),userTrainer.getEmail())).ccEmails(List.of())
                .body("Training Details are :\n"+
                        "Trainee Name :"+trainingDetails.getTraineeUserName()+"\n"
                        +"Trainer Name :"+trainingDetails.getTrainerUserName()+"\n"
                        +"Training Name :"+trainingDetails.getTrainingName()+"\n"
                        +"Training Type :"+trainingDetails.getTrainingName()+"\n"
                        +"Training Date :"+trainingDetails.getTrainingDate()+"\n"
                        +"Training Duration :"+trainingDetails.getTrainingDuration()).build();
		
		notificationProxy.addNewNotificationLog(notificationDto);
		
		return trainingReportDto;
	}

	

}
