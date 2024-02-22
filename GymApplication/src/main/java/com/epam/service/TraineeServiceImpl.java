package com.epam.service;

import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.Training;
import com.epam.entities.User;
import com.epam.proxy.NotificationProxy;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epam.customexceptions.UserException;
import com.epam.dtos.request.TraineeDetailsDto;
import com.epam.dtos.request.TraineeProfileUpdate;
import com.epam.dtos.request.TraineeTrainingDto;
import com.epam.dtos.response.NotificationDto;
import com.epam.dtos.response.TraineeBasicDetailsDto;
import com.epam.dtos.response.TraineeProfileDto;
import com.epam.dtos.response.TrainerDto;
import com.epam.dtos.response.TraineeTrainingResponseDto;
import com.epam.repository.TraineeRepository;
import com.epam.repository.UserRepository;
import com.epam.utilities.PasswordGenerator;
import com.epam.utilities.UserNameGenerator;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TraineeServiceImpl implements TraineeService{
	
	@Autowired
	TraineeRepository traineeRepo;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	UserNameGenerator userNameGenerator;
	
	@Autowired
	PasswordGenerator passwordGenerator;
	
	@Autowired
	NotificationProxy notificationProxy;
	
	private String errormessage = "Invalid Username!!";

	public TraineeBasicDetailsDto saveTrainee(TraineeDetailsDto traineeDetails) {
		log.info("Entered into the Trainee Service - saveTrainee() method to save a Trainee into the library");
		User newuser = User.builder()
					  .firstName(traineeDetails.getFirstName())
					  .lastName(traineeDetails.getLastName())
					  .email(traineeDetails.getEmail())
					  .isActive(false)
					  .userName(userNameGenerator.generateUserName(traineeDetails.getEmail()))
					  .password(passwordGenerator.generatePassword())
					  .build();
		
		newuser = userRepo.save(newuser);
		Trainee trainee = Trainee.builder()
				 .dateOfBirth(traineeDetails.getDateOfBirth())
	   			 .address(traineeDetails.getAddress())
	   			 .build();
		trainee.setUser(newuser);
		traineeRepo.save(trainee); 
		
		NotificationDto notificationDto=NotificationDto.builder().subject("Trainee is registered successfully!!").toEmails(List.of(trainee.getUser().getEmail())).ccEmails(List.of())
                .body("The Trainee Details are :\n"
                        +"User Name : "+newuser.getUserName()+"\n"
                        +"Password : "+newuser.getPassword()+"\n"
                        +"First Name : "+newuser.getFirstName()+"\n"
                        +"Last Name : "+newuser.getLastName()+"\n"
                        +"Address : "+traineeDetails.getAddress()+"\n"
                        +"Email : "+newuser.getEmail()+"\n"
                        +"Status : "+newuser.isActive())
				.build();
				
		notificationProxy.addNewNotificationLog(notificationDto);
				
		return TraineeBasicDetailsDto.builder()
							.userName(newuser.getUserName())                                                                                                                  
							.password(newuser.getPassword())
							.build();
	}

	@Override
	public TraineeProfileDto getTrainee(String username) throws UserException {
		log.info("Entered into the Trainee Service - getTrainee() method to get a Trainee with username : {} ",username);
		User user = userRepo.findByUserName(username).orElseThrow(()->new UserException(errormessage));
		Trainee trainee = user.getTrainee();
		TraineeProfileDto traineeProfile = TraineeProfileDto.builder()
						 .firstName(user.getFirstName())
						 .lastName(user.getLastName())
						 .dateOfBirth(trainee.getDateOfBirth())
						 .address(trainee.getAddress())
						 .isActive(user.isActive())
						 .build();
		
		List<TrainerDto> trainerDtoList = trainee.getTrainersList().stream()
			.map(trainer->TrainerDto.builder()
						  .userName(trainer.getUser().getUserName())
						  .firstName(trainer.getUser().getFirstName())
						  .lastName(trainer.getUser().getLastName())
						  .trainingType(trainer.getTrainingType().getTrainingTypeName())
						  .build())
			   .toList();
		
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
		
		List<TrainerDto> trainerDtoList = user.getTrainee().getTrainersList().stream()
				.map(trainer->TrainerDto.builder()
							  .userName(trainer.getUser().getUserName())
							  .firstName(trainer.getUser().getFirstName())
							  .lastName(trainer.getUser().getLastName())
							  .trainingType(trainer.getTrainingType().getTrainingTypeName())
							  .build())
				   .toList();
		
		NotificationDto notificationDto=NotificationDto.builder().subject("Trainee profile is updated successfully!!").toEmails(List.of(user.getEmail())).ccEmails(List.of())
                .body("The updated Trainee Details are :\n"
                        +"User Name : "+user.getUserName()+"\n"
                        +"Password : "+user.getPassword()+"\n"
                        +"First Name : "+traineeProfile.getFirstName()+"\n"
                        +"Last Name : "+traineeProfile.getLastName()+"\n"
                        +"Address : "+traineeProfile.getAddress()+"\n"
                        +"Email : "+user.getEmail()+"\n"
                        +"Status : "+traineeProfile.isActive())
				.build();
				
		notificationProxy.addNewNotificationLog(notificationDto);
			
		return TraineeProfileDto.builder()
								.firstName(traineeProfile.getFirstName())
								.lastName(traineeProfile.getLastName())
								.dateOfBirth(traineeProfile.getDateOfBirth())
								.address(traineeProfile.getAddress())
								.isActive(traineeProfile.isActive())
								.trainersList(trainerDtoList)
								.build();
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
		
		return trainersList.stream()
					.map(trainer -> TrainerDto.builder()
							  		.userName(trainer.getUser().getUserName())
							  		.firstName(trainer.getUser().getFirstName())
							  		.lastName(trainer.getUser().getLastName())
							  		.trainingType(trainer.getTrainingType().getTrainingTypeName())
							  		.build())
					.toList();
	}

	@Override
	public List<TraineeTrainingResponseDto> getTraineeTrainingsList(TraineeTrainingDto traineeTraining) throws UserException {
		log.info("Entered into the Trainee Service - getTraineeTrainingsList() method to get Trainee Trainings list");
		User user = userRepo.findByUserName(traineeTraining.getUsername()).orElseThrow(()->new UserException(errormessage));
		TraineeTrainingResponseDto trainingDto;
		List<TraineeTrainingResponseDto> trainingDtoList = new ArrayList<>();
		Trainee trainee = user.getTrainee();
		for(Training training : trainee.getTrainingsList()) {
			if(((traineeTraining.getTrainerName()==null)||training.getTrainer().getUser().getUserName().equals(traineeTraining.getTrainerName()))&&(traineeTraining.getPeriodFrom()==null || traineeTraining.getPeriodFrom().equals(training.getTrainingDate()))
					&&((traineeTraining.getTrainingType()==null)||training.getTrainingType().getTrainingTypeName().equals(traineeTraining.getTrainingType()))) {
				trainingDto = TraineeTrainingResponseDto.builder()
						   .trainingName(training.getTrainingName())
						   .trainingDate(training.getTrainingDate())
						   .trainingType(training.getTrainingType().getTrainingTypeName())
						   .trainingDuration(training.getTrainingDuration())
						   .trainerName(training.getTrainer().getUser().getUserName())
						   .build(); 
				
			trainingDtoList.add(trainingDto);
			}
		
		}
		return trainingDtoList;
	}
	
}

