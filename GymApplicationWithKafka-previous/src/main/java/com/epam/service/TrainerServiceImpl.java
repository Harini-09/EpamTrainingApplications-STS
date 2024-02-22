package com.epam.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.epam.customexceptions.TrainerException;
import com.epam.customexceptions.TrainingTypeException;
import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TrainerDetailsDto;
import com.epam.dtos.request.TrainerProfileUpdateDto;
import com.epam.dtos.response.NotificationDto;
import com.epam.dtos.response.TraineeDto;
import com.epam.dtos.response.TrainerBasicDetailsDto;
import com.epam.dtos.response.TrainerDto;
import com.epam.dtos.response.TrainerProfileDto;
import com.epam.dtos.response.TrainerTrainingResponseDto;
import com.epam.entities.Trainer;
import com.epam.entities.Training;
import com.epam.entities.TrainingType;
import com.epam.entities.User;
import com.epam.repository.TrainerRepository;
import com.epam.repository.TrainingTypeRepository;
import com.epam.repository.UserRepository;
import com.epam.utilities.PasswordGenerator;
import com.epam.utilities.TrainerValueMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainerServiceImpl implements TrainerService{
	
	@Autowired
	TrainerRepository trainerRepo;

	@Autowired
	UserRepository userRepo;
	
	@Autowired
	TrainingTypeRepository trainingTypeRepo;
	
	@Autowired
	PasswordGenerator passwordGenerator;
	
	@Autowired
	TrainerValueMapper mapper;
	
	@Value("${errormessage}")
	private String errormessage;
	
	@Autowired
	private KafkaTemplate<String,NotificationDto> kafkaTemplate;
	
	public TrainerBasicDetailsDto saveTrainer(TrainerDetailsDto trainerDetails) throws TrainingTypeException {
		log.info("Entered into the Trainer Service - saveTrainer() method to save a Trainer into the library");
		String randomPassword = passwordGenerator.generatePassword();
		User newuser = mapper.convertToUser(trainerDetails, randomPassword);
		Trainer trainer = new Trainer();
		TrainingType trainingType = trainingTypeRepo.findByTrainingTypeName(trainerDetails.getTrainingTypeName()).orElseThrow(()->new TrainingTypeException("The Training Type is not present!!"));
		trainer.setTrainingType(trainingType);
		newuser = userRepo.save(newuser);
		trainer.setUser(newuser);
		trainerRepo.save(trainer);
		NotificationDto notificationDto=mapper.convertToNotificationDtoAfterSavingTrainer(newuser, trainer, trainerDetails);
		Message<NotificationDto> message = mapper.convertToMessage(notificationDto);
		kafkaTemplate.send(message);		
		return mapper.convertToTrainerBasicDetailsDto(newuser,randomPassword);
	}
   
	@Override
	public TrainerProfileDto getTrainer(String username) throws UserException {
		log.info("Entered into the Trainer Service - getTrainer() method to get a Trainer with username : {} ",username);
		User user = userRepo.findByUserName(username).orElseThrow(()->new UserException(errormessage));
		Trainer trainer = user.getTrainer();
		List<TraineeDto> traineeDtoList = mapper.convertToTraineeDtoList(trainer);			
		return mapper.convertToTrainerProfileDto(user, trainer, traineeDtoList);
	}

	@Override
	@Transactional
	public TrainerProfileDto updateTrainer(TrainerProfileUpdateDto trainerProfile) throws UserException {
		log.info("Entered into the Trainer Service - updateTrainer() method to update a Trainer");
		User user = userRepo.findByUserName(trainerProfile.getUsername()).orElseThrow(()->new UserException(errormessage));
		user.setFirstName(trainerProfile.getFirstName());
		user.setLastName(trainerProfile.getLastName());
		user.setActive(trainerProfile.isActive());
		userRepo.save(user);
		Trainer trainer = user.getTrainer();
		List<TraineeDto> traineeDtoList = mapper.convertToTraineeDtoListForUpdate(trainer);
		NotificationDto notificationDto=mapper.convertToNotificationDtoAfterUpdatingTrainer(user);
		Message<NotificationDto> message = mapper.convertToMessage(notificationDto);
		kafkaTemplate.send(message);
		return mapper.convertToTrainerProfileDtoForUpdate(user, trainer, traineeDtoList);
	} 
 
	@Override
	public TrainerDto getNotAssignedActiveTrainers(String username) throws UserException, TrainerException{
		log.info("Entered into the Trainer Service - getNotAssignedActiveTrainers() method to get not assigned on active Trainers");
		User user = userRepo.findByUserName(username).orElseThrow(()->new UserException(errormessage));
		List<Trainer> trainers = (List<Trainer>) trainerRepo.findAll();
		return mapper.convertToTrainerDto(trainers, user);
	} 
 
	@Override
	public List<TrainerTrainingResponseDto> getTrainerTrainingsList(String username, LocalDate periodFrom,
			LocalDate periodTo, String traineeName) throws UserException {
		log.info("Entered into the Trainer Service - getTrainerTrainingsList() method to get Trainer Trainings list");
		userRepo.findByUserName(username).orElseThrow(()->new UserException(errormessage));
		List<Training> trainerTrainings = trainerRepo.findTrainingsForTrainer(username, periodFrom, periodTo, traineeName);
		return mapper.convertToTrainerTrainingsList(trainerTrainings);	
	} 

}
