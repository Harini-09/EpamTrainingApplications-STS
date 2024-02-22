package com.epam.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.epam.utilities.UserNameGenerator;

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
	UserNameGenerator userNameGenerator;
	
	@Autowired
	TrainingTypeRepository trainingTypeRepo;
	
	@Autowired
	PasswordGenerator passwordGenerator;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private String errormessage = "Invalid Username!!";
	
	@Autowired
	private KafkaTemplate<String,NotificationDto> kafkaTemplate;
	
	public TrainerBasicDetailsDto saveTrainer(TrainerDetailsDto trainerDetails) throws TrainingTypeException {
		log.info("Entered into the Trainer Service - saveTrainer() method to save a Trainer into the library");
		User newuser = User.builder()
				  .firstName(trainerDetails.getFirstName())
				  .lastName(trainerDetails.getLastName())
				  .isActive(true)
				  .email(trainerDetails.getEmail())				  
				  .userName(userNameGenerator.generateUserName(trainerDetails.getEmail()))
				  .password(passwordEncoder.encode("1234"))
				  .build();
		
		Trainer trainer = new Trainer();
		TrainingType trainingType = trainingTypeRepo.findByTrainingTypeName(trainerDetails.getTrainingTypeName()).orElseThrow(()->new TrainingTypeException("The Training Type is not present!!"));
		trainer.setTrainingType(trainingType);
		
		newuser = userRepo.save(newuser);
		trainer.setUser(newuser);
		
		trainerRepo.save(trainer);
		
		NotificationDto notificationDto=NotificationDto.builder().subject("Trainer is registered successfully!!").toEmails(List.of(trainer.getUser().getEmail())).ccEmails(List.of())
                .body("The Trainer Details are :\n"
                        +"User Name : "+newuser.getUserName()+"\n"
                        +"Password : "+newuser.getPassword()+"\n"
                        +"First Name : "+newuser.getFirstName()+"\n"
                        +"Last Name : "+newuser.getLastName()+"\n"
                        +"Email : "+newuser.getEmail()+"\n"
                        +"Training Type : "+trainerDetails.getTrainingTypeName()+"\n"
                        +"Status : "+newuser.isActive())
				.build();
				
		Message<NotificationDto> message = MessageBuilder.withPayload(notificationDto)
				 										 .setHeader(KafkaHeaders.TOPIC, "notificationtopic")
				 										 .build();
		kafkaTemplate.send(message);
		
		return TrainerBasicDetailsDto.builder()
				.userName(newuser.getUserName())                                                                                                                  
				.password(newuser.getPassword())
				.build();
		
	}

	@Override
	public TrainerProfileDto getTrainer(String username) throws UserException {
		log.info("Entered into the Trainer Service - getTrainer() method to get a Trainer with username : {} ",username);
		User user = userRepo.findByUserName(username).orElseThrow(()->new UserException(errormessage));
		Trainer trainer = user.getTrainer();

		List<TraineeDto> traineeDtoList = trainer.getTraineesList().stream()
								.map(trainee->TraineeDto.builder()
										   .username(trainee.getUser().getUserName())
										   .firstName(trainee.getUser().getFirstName())
										   .lastName(trainee.getUser().getLastName())
										   .build())
								.toList();		
		
		return TrainerProfileDto.builder()
								.firstName(user.getFirstName())
								.lastName(user.getLastName())
								.trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
								.isActive(user.isActive())
								.traineesList(traineeDtoList)
								.build();
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
		
		List<TraineeDto> traineeDtoList = trainer.getTraineesList().stream()
						   .map(trainee->TraineeDto.builder()
						   .username(trainee.getUser().getUserName())
						   .firstName(trainee.getUser().getFirstName())
						   .lastName(trainee.getUser().getLastName())
						   .build())
						   .toList();	
		
		NotificationDto notificationDto=NotificationDto.builder().subject("Trainer profile is updated successfully!!").toEmails(List.of(user.getEmail())).ccEmails(List.of())
                .body("The updated Trainee Details are :\n"
                        +"User Name : "+user.getUserName()+"\n"
                        +"Password : "+user.getPassword()+"\n"
                        +"First Name : "+user.getFirstName()+"\n"
                        +"Last Name : "+user.getLastName()+"\n"
                        +"Email : "+user.getEmail()+"\n"
                        +"Training Type : "+user.getTrainer().getTrainingType()+"\n"
                        +"Status : "+user.isActive())
				.build();
				
		Message<NotificationDto> message = MessageBuilder.withPayload(notificationDto)
				 										 .setHeader(KafkaHeaders.TOPIC, "notificationtopic")
				 										 .build();
		kafkaTemplate.send(message);
			
		return TrainerProfileDto.builder()
								.firstName(user.getFirstName())
								.lastName(user.getLastName())
								.trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
								.isActive(user.isActive())
								.traineesList(traineeDtoList)
								.build();
	}

	@Override
	public TrainerDto getNotAssignedActiveTrainers(String username) throws UserException, TrainerException{
		log.info("Entered into the Trainer Service - getNotAssignedActiveTrainers() method to get not assigned on active Trainers");
		User user = userRepo.findByUserName(username).orElseThrow(()->new UserException(errormessage));
		List<Trainer> trainers = (List<Trainer>) trainerRepo.findAll();

		return trainers.stream()
				   .filter(trainer->trainer.getUser().isActive() && (!trainer.getTraineesList().contains(user.getTrainee())))
				   .map(trainer->TrainerDto.builder()
							 .userName(trainer.getUser().getUserName())
							 .firstName(trainer.getUser().getFirstName())
							 .lastName(trainer.getUser().getLastName())
							 .trainingType(trainer.getTrainingType().getTrainingTypeName())
							 .build())
				   .findFirst()
				   .orElseThrow(()->new TrainerException("No Trainer is available!!"));
		
	} 

	@Override
	public List<TrainerTrainingResponseDto> getTrainerTrainingsList(String username, LocalDate periodFrom,
			LocalDate periodTo, String traineeName) throws UserException {
		log.info("Entered into the Trainer Service - getTrainerTrainingsList() method to get Trainer Trainings list");
		userRepo.findByUserName(username).orElseThrow(()->new UserException(errormessage));
		List<Training> trainerTrainings = trainerRepo.findTrainingsForTrainer(username, periodFrom, periodTo, traineeName);
		return trainerTrainings.stream()
						.map(training->TrainerTrainingResponseDto.builder()
										 .trainingName(training.getTrainingName())
										 .trainingDate(training.getTrainingDate())
										 .trainingType(training.getTrainingType().getTrainingTypeName())
										 .trainingDuration(training.getTrainingDuration())
										 .traineeName(training.getTrainee().getUser().getUserName())
										 .build())
						.toList();		
	} 

}
