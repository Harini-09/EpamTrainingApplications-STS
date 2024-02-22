package com.epam.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TraineeDetailsDto;
import com.epam.dtos.request.TraineeProfileUpdate;
import com.epam.dtos.response.NotificationDto;
import com.epam.dtos.response.TraineeBasicDetailsDto;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TraineeTrainingResponseDto;
import com.epam.dtos.response.TrainerDto;
import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.Training;
import com.epam.entities.User;
import com.epam.repository.TraineeRepository;
import com.epam.repository.UserRepository;
import com.epam.utilities.PasswordGenerator;
import com.epam.utilities.TraineeValueMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService{
	 
	@Autowired
	private TraineeRepository traineeRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordGenerator passwordGenerator;
	
	@Autowired
	private TraineeValueMapper mapper;
		
	@Value("${errormessage}")
	private String errormessage;
	
//	@Autowired
//	private KafkaTemplate<String,NotificationDto> kafkaTemplate;

	public TraineeBasicDetailsDto saveTrainee(TraineeDetailsDto traineeDetails) {
		log.info("Entered into the Trainee Service - saveTrainee() method to save a Trainee into the library");
		String randomPassword = passwordGenerator.generatePassword();
		User newuser = mapper.convertToUser(traineeDetails, randomPassword);	
		newuser = userRepo.save(newuser);
		Trainee trainee = Trainee.builder()
				 .dateOfBirth(traineeDetails.getDateOfBirth())
	   			 .address(traineeDetails.getAddress())
	   			 .build(); 
		trainee.setUser(newuser); 
		traineeRepo.save(trainee); 
		NotificationDto notificationDto=mapper.convertToNotificationDtoAfterSavingTrainee(newuser,randomPassword,traineeDetails,trainee);
		Message<NotificationDto> message = mapper.convertToMessage(notificationDto);
		//kafkaTemplate.send(message);
		return mapper.convertToTraineeBasicDetailsDto(newuser, randomPassword);
	} 
      
	@Override
	public TraineeProfileDto getTrainee(String username) throws UserException {
		log.info("Entered into the Trainee Service - getTrainee() method to get a Trainee with username : {} ",username);
		User user = userRepo.findByUserName(username).orElseThrow(()->new UserException(errormessage));
		Trainee trainee = user.getTrainee();
		TraineeProfileDto traineeProfile = mapper.convertToTraineeProfileDto(user, trainee);
		List<TrainerDto> trainerDtoList = mapper.convertToTrainerDtoList(trainee);
		traineeProfile.setTrainersList(trainerDtoList);
		return traineeProfile;
		
	} 

	@Override
	@Transactional
	public TraineeProfileDto updateTrainee(TraineeProfileUpdate traineeProfile) throws UserException {
		log.info("Entered into the Trainee Service - updateTrainee() method to update a Trainee");
		User user = userRepo.findByUserName(traineeProfile.getUsername()).orElseThrow(()->new UserException(errormessage));
		user.setUserName(traineeProfile.getUsername());
		user.setFirstName(traineeProfile.getFirstName());
		user.setLastName(traineeProfile.getLastName());
		user.getTrainee().setDateOfBirth(traineeProfile.getDateOfBirth());
		user.getTrainee().setAddress(traineeProfile.getAddress());
		user.setActive(traineeProfile.isActive());
		userRepo.save(user);
		List<TrainerDto> trainerDtoList = mapper.convertToTrainerDtoList(user);	
		NotificationDto notificationDto=mapper.convertToNotificationDtoAfterUpdatingTrainee(user,traineeProfile);				
		Message<NotificationDto> message = mapper.convertToMessage(notificationDto);
		//kafkaTemplate.send(message);
		return mapper.convertToTraineeProfileDto(traineeProfile, trainerDtoList);
	}
  
	@Override
	public void deleteTrainee(String username) throws UserException {
		log.info("Entered into the Trainee Service - deleteTrainee() method to delete a Trainee with username : {}",username);
		User user = userRepo.findByUserName(username).orElseThrow(()->new UserException("Invalid Username!!"));
		userRepo.delete(user);
	}
 
	@Override
	@Transactional
	public List<TrainerDto> updateTraineeTrainerList(String traineeUserName,List<String> trainerUserNames) throws UserException {
		log.info("Entered into the Trainee Service - updateTraineeTrainerList() method to update a Trainee Trainers list");
		User user = userRepo.findByUserName(traineeUserName).orElseThrow(()->new UserException(errormessage));
		List<Trainer> trainersList = new ArrayList<>();
		for(String trainerUserName : trainerUserNames) {
			User newuser = userRepo.findByUserName(trainerUserName).orElseThrow(()->new UserException("One of the Invalid Usernames for trainers!!"));
			trainersList.add(newuser.getTrainer());
		}
		user.getTrainee().setTrainersList(trainersList);
		userRepo.save(user);
		return mapper.convertToTrainerDtoList(trainersList);
	} 
  
	@Override
	public List<TraineeTrainingResponseDto> getTraineeTrainingsList(String username, LocalDate periodFrom,
			LocalDate periodTo, String trainerName, String trainingType) throws UserException {
		log.info("Entered into the Trainee Service - getTraineeTrainingsList() method to get a list of Trainings for a particular Trainee");
		userRepo.findByUserName(username).orElseThrow(()->new UserException(errormessage));
		List<Training> traineeTrainings = traineeRepo.findTrainingsForTrainee(username, periodFrom, periodTo, trainerName, trainingType);
		return mapper.convertToTraineeTrainingResponseDto(traineeTrainings);
	}
	
}  

