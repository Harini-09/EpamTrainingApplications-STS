package com.epam.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
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
import com.epam.repository.TrainingRepository;
import com.epam.repository.TrainingTypeRepository;
import com.epam.repository.UserRepository;
import com.epam.utilities.TrainingValueMapper;

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
	private KafkaTemplate<String,NotificationDto> notificationKafkaTemplate;
	
	@Autowired
	private KafkaTemplate<String,TrainingReportDto> reportKafkaTemplate;
	
	@Autowired
	TrainingValueMapper mapper;
	
	@Override
	public void saveTraining(TrainingDetailsDto trainingDetails) throws UserException, TrainingTypeException, TrainerException {
		log.info("Entered into the Training Service - saveTraining() method to save a Training into the library");
		Training training = new Training();
		User userTrainee = userRepo.findByUserName(trainingDetails.getTraineeUserName()).orElseThrow(()->new UserException("Invalid Username for Trainee!!"));			 
		userTrainee.getTrainee().getTrainersList()
				.stream()
				.filter(trainer->trainer.getUser().getUserName().equals(trainingDetails.getTrainerUserName()))
				.findAny()
				.orElseThrow(()->new TrainerException("The entered Trainer is not allocated to this particular Trainee"));
 	  
		training.setTrainee(userTrainee.getTrainee());
		Optional<User> userTrainerOptional = userRepo.findByUserName(trainingDetails.getTrainerUserName());
		User userTrainer = userTrainerOptional.get();		
		if(!userTrainer.getTrainer().getTrainingType().getTrainingTypeName().equals(trainingDetails.getTrainingTypeName())){
			throw new TrainerException("The entered Training Type is not allocated to this particular trainer");
		}	  		 	  
		training.setTrainer(userTrainer.getTrainer());
		training.setTrainingName(trainingDetails.getTrainingName());
		training.setTrainingDate(trainingDetails.getTrainingDate());
		Optional<TrainingType> trainingType = trainingTypeRepo.findByTrainingTypeName(trainingDetails.getTrainingTypeName());
		training.setTrainingType(trainingType.get());
		training.setTrainingDuration(trainingDetails.getTrainingDuration());		
		TrainingReportDto trainingReportDto = mapper.convertToTrainingReportDto(trainingDetails, userTrainer);
		trainingRepo.save(training);		
		NotificationDto notificationDto=mapper.convertToNotificationDto(trainingDetails, userTrainee, userTrainer);		
		Message<NotificationDto> notificationMessage = mapper.convertToNotificationMessage(notificationDto);
		notificationKafkaTemplate.send(notificationMessage);
		Message<TrainingReportDto> reportMessage = mapper.convertToReportMessage(trainingReportDto);
		reportKafkaTemplate.send(reportMessage);
		
	} 
 
} 
