package com.epam.utilities;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.epam.customexceptions.TrainerException;
import com.epam.dtos.request.TrainerDetailsDto;
import com.epam.dtos.response.NotificationDto;
import com.epam.dtos.response.TraineeDto;
import com.epam.dtos.response.TrainerBasicDetailsDto;
import com.epam.dtos.response.TrainerDto;
import com.epam.dtos.response.TrainerProfileDto;
import com.epam.dtos.response.TrainerTrainingResponseDto;
import com.epam.entities.Trainer;
import com.epam.entities.Training;
import com.epam.entities.User;

@Service
public class TrainerValueMapper {
	
	@Autowired
	private UserNameGenerator userNameGenerator;
	
	@Autowired
	private PasswordEncoder encoder;

	public User convertToUser(TrainerDetailsDto trainerDetails,String randomPassword) {
		return User.builder()
				  .firstName(trainerDetails.getFirstName())
				  .lastName(trainerDetails.getLastName())
				  .isActive(true)
				  .email(trainerDetails.getEmail())				  
				  .userName(userNameGenerator.generateUserName(trainerDetails.getEmail()))
				  .password(encoder.encode(randomPassword))
				  .build();
	}
	 
	public NotificationDto convertToNotificationDtoAfterSavingTrainer(User newuser,Trainer trainer,TrainerDetailsDto trainerDetails) {
		return NotificationDto.builder().subject("Trainer is registered successfully!!").toEmails(List.of(trainer.getUser().getEmail())).ccEmails(List.of())
                .body("The Trainer Details are :\n"
                        +"User Name : "+newuser.getUserName()+"\n"
                        +"Password : "+newuser.getPassword()+"\n"
                        +"First Name : "+newuser.getFirstName()+"\n"
                        +"Last Name : "+newuser.getLastName()+"\n"
                        +"Email : "+newuser.getEmail()+"\n"
                        +"Training Type : "+trainerDetails.getTrainingTypeName()+"\n"
                        +"Status : "+newuser.isActive())
				.build();
	}
	
	public TrainerBasicDetailsDto convertToTrainerBasicDetailsDto(User newuser, String randomPassword) {
		return TrainerBasicDetailsDto.builder()
				.userName(newuser.getUserName())                                                                                                                  
				.password(randomPassword)
				.build();
	}
	
	public List<TraineeDto> convertToTraineeDtoList(Trainer trainer) {
		return trainer.getTraineesList().stream()
				.map(trainee->TraineeDto.builder()
						   .username(trainee.getUser().getUserName())
						   .firstName(trainee.getUser().getFirstName())
						   .lastName(trainee.getUser().getLastName())
						   .build())
				.toList();
	}
	
	public TrainerProfileDto convertToTrainerProfileDto(User user,Trainer trainer,List<TraineeDto> traineeDtoList) {
		return TrainerProfileDto.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
				.isActive(user.isActive())
				.traineesList(traineeDtoList)
				.build();
	}
	
	public List<TraineeDto> convertToTraineeDtoListForUpdate(Trainer trainer){
		return trainer.getTraineesList().stream()
				   .map(trainee->TraineeDto.builder()
				   .username(trainee.getUser().getUserName())
				   .firstName(trainee.getUser().getFirstName())
				   .lastName(trainee.getUser().getLastName())
				   .build())
				   .toList();
	}
	
	public NotificationDto convertToNotificationDtoAfterUpdatingTrainer(User user) {
		return NotificationDto.builder().subject("Trainer profile is updated successfully!!").toEmails(List.of(user.getEmail())).ccEmails(List.of())
                .body("The updated Trainee Details are :\n"
                        +"User Name : "+user.getUserName()+"\n"
                        +"Password : "+user.getPassword()+"\n"
                        +"First Name : "+user.getFirstName()+"\n"
                        +"Last Name : "+user.getLastName()+"\n"
                        +"Email : "+user.getEmail()+"\n"
                        +"Training Type : "+user.getTrainer().getTrainingType()+"\n"
                        +"Status : "+user.isActive())
				.build();
	}
	
	 
	public TrainerProfileDto convertToTrainerProfileDtoForUpdate(User user,Trainer trainer,List<TraineeDto> traineeDtoList) {
		return TrainerProfileDto.builder()
				.firstName(user.getFirstName())
				.lastName(user.getLastName())
				.trainingTypeName(trainer.getTrainingType().getTrainingTypeName())
				.isActive(user.isActive())
				.traineesList(traineeDtoList)
				.build();
	}
	
	public TrainerDto convertToTrainerDto(List<Trainer> trainers,User user) throws TrainerException {
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
	
	public List<TrainerTrainingResponseDto> convertToTrainerTrainingsList(List<Training> trainerTrainings){
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
	
	public Message<NotificationDto> convertToMessage(NotificationDto notificationDto){
		return MessageBuilder.withPayload(notificationDto)
 				 .setHeader(KafkaHeaders.TOPIC, "notification_topic")
				 .build();
	}
	 
} 
