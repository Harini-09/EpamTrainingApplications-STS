package com.epam.utilities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

@Service
public class TraineeValueMapper {
	
	@Autowired
	private UserNameGenerator userNameGenerator;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public User convertToUser(TraineeDetailsDto traineeDetails,String randomPassword) {
		return User.builder()
				  .firstName(traineeDetails.getFirstName())
				  .lastName(traineeDetails.getLastName())
				  .email(traineeDetails.getEmail())
				  .isActive(true)
				  .userName(userNameGenerator.generateUserName(traineeDetails.getEmail()))
				  .password(encoder.encode(randomPassword))
				  .build();
	}

	public NotificationDto convertToNotificationDtoAfterSavingTrainee(User newuser,String randomPassword,TraineeDetailsDto traineeDetails,Trainee trainee) {
		return NotificationDto.builder().subject("Trainee is registered successfully!!").toEmails(List.of(trainee.getUser().getEmail())).ccEmails(List.of())
                .body("The Trainee Details are :\n"
                        +"User Name : "+newuser.getUserName()+"\n"
                        +"Password : "+randomPassword+"\n"
                        +"First Name : "+newuser.getFirstName()+"\n"
                        +"Last Name : "+newuser.getLastName()+"\n"
                        +"Address : "+traineeDetails.getAddress()+"\n"
                        +"Email : "+newuser.getEmail()+"\n"
                        +"Status : "+newuser.isActive())
				.build(); 
	}
	 
	public TraineeBasicDetailsDto convertToTraineeBasicDetailsDto(User newuser,String randomPassword) {
		return TraineeBasicDetailsDto.builder()
				.userName(newuser.getUserName())                                                                                                                  
				.password(randomPassword)
				.build();
	}
	
	public TraineeProfileDto convertToTraineeProfileDto(User user,Trainee trainee) {
		return TraineeProfileDto.builder()
				 .firstName(user.getFirstName())
				 .lastName(user.getLastName())
				 .dateOfBirth(trainee.getDateOfBirth())
				 .address(trainee.getAddress())
				 .isActive(user.isActive())
				 .build();
	}
	
	public List<TrainerDto> convertToTrainerDtoList(Trainee trainee){
		return trainee.getTrainersList().stream()
				.map(trainer->TrainerDto.builder()
						  .userName(trainer.getUser().getUserName())
						  .firstName(trainer.getUser().getFirstName())
						  .lastName(trainer.getUser().getLastName())
						  .trainingType(trainer.getTrainingType().getTrainingTypeName())
						  .build())
			   .toList();
	}
	
	public List<TrainerDto> convertToTrainerDtoList(User user){
		return user.getTrainee().getTrainersList().stream()
				.map(trainer->TrainerDto.builder()
						  .userName(trainer.getUser().getUserName())
						  .firstName(trainer.getUser().getFirstName())
						  .lastName(trainer.getUser().getLastName())
						  .trainingType(trainer.getTrainingType().getTrainingTypeName())
						  .build())
			   .toList();

	}
	
	public NotificationDto convertToNotificationDtoAfterUpdatingTrainee(User user,TraineeProfileUpdate traineeProfile) {
		return NotificationDto.builder().subject("Trainee profile is updated successfully!!").toEmails(List.of(user.getEmail())).ccEmails(List.of())
                .body("The updated Trainee Details are :\n"
                        +"User Name : "+user.getUserName()+"\n"
                        +"Password : "+user.getPassword()+"\n"
                        +"First Name : "+traineeProfile.getFirstName()+"\n"
                        +"Last Name : "+traineeProfile.getLastName()+"\n"
                        +"Address : "+traineeProfile.getAddress()+"\n"
                        +"Email : "+user.getEmail()+"\n"
                        +"Status : "+traineeProfile.isActive())
				.build();
		
	}
	 
	public TraineeProfileDto convertToTraineeProfileDto(TraineeProfileUpdate traineeProfile,List<TrainerDto> trainerDtoList) {
		return TraineeProfileDto.builder()
				.firstName(traineeProfile.getFirstName())
				.lastName(traineeProfile.getLastName())
				.dateOfBirth(traineeProfile.getDateOfBirth())
				.address(traineeProfile.getAddress())
				.isActive(traineeProfile.isActive())
				.trainersList(trainerDtoList)
				.build();
	}
	
	public List<TrainerDto> convertToTrainerDtoList(List<Trainer> trainersList){
		return trainersList.stream()
				.map(trainer -> TrainerDto.builder()
						  		.userName(trainer.getUser().getUserName())
						  		.firstName(trainer.getUser().getFirstName())
						  		.lastName(trainer.getUser().getLastName())
						  		.trainingType(trainer.getTrainingType().getTrainingTypeName())
						  		.build())
				.toList();
	}
	
	public List<TraineeTrainingResponseDto> convertToTraineeTrainingResponseDto(List<Training> traineeTrainings){
		return traineeTrainings.stream()
				.map(training->TraineeTrainingResponseDto.builder()
		   												 .trainingName(training.getTrainingName())
		   												 .trainingDate(training.getTrainingDate())
		   												 .trainingType(training.getTrainingType().getTrainingTypeName())
		   												 .trainingDuration(training.getTrainingDuration())
		   												 .trainerName(training.getTrainer().getUser().getUserName())
		   												 .build())
				.toList(); 
		 
	}
	
	public Message<NotificationDto> convertToMessage(NotificationDto notificationDto){
		return MessageBuilder.withPayload(notificationDto)
 				 .setHeader(KafkaHeaders.TOPIC, "notification_topic")
				 .build();
	}
	
	
	
	
	
}
