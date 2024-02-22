package com.epam.utilities;

import java.util.List;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.epam.dtos.request.TrainingDetailsDto;
import com.epam.dtos.response.NotificationDto;
import com.epam.dtos.response.TrainingReportDto;
import com.epam.entities.User;

@Service
public class TrainingValueMapper {

	public TrainingReportDto convertToTrainingReportDto(TrainingDetailsDto trainingDetails,User userTrainer) {
		return TrainingReportDto.builder()
				 .trainerUserName(trainingDetails.getTrainerUserName())
				 .trainerFirstName(userTrainer.getFirstName())
				 .trainerLastName(userTrainer.getLastName())
				 .isTrainerActive(userTrainer.isActive())
				 .trainerEmail(userTrainer.getEmail())
				 .date(trainingDetails.getTrainingDate())
				 .duration(trainingDetails.getTrainingDuration())
				 .build();
	}  
	
	public NotificationDto convertToNotificationDto(TrainingDetailsDto trainingDetails,User userTrainee,User userTrainer) {
		return NotificationDto.builder().subject("Training is added Successfully").toEmails(List.of(userTrainee.getEmail(),userTrainer.getEmail())).ccEmails(List.of())
                .body("Training Details are :\n"+
                        "Trainee Name :"+trainingDetails.getTraineeUserName()+"\n"
                        +"Trainer Name :"+trainingDetails.getTrainerUserName()+"\n"
                        +"Training Name :"+trainingDetails.getTrainingName()+"\n"
                        +"Training Type :"+trainingDetails.getTrainingName()+"\n"
                        +"Training Date :"+trainingDetails.getTrainingDate()+"\n"
                        +"Training Duration :"+trainingDetails.getTrainingDuration()).build();
	}
	
	public Message<NotificationDto> convertToNotificationMessage(NotificationDto notificationDto){
		return MessageBuilder.withPayload(notificationDto)
 				 .setHeader(KafkaHeaders.TOPIC, "notification_topic")
				 .build();
	}
	
	public Message<TrainingReportDto> convertToReportMessage(TrainingReportDto trainingReportDto){
		return MessageBuilder.withPayload(trainingReportDto)
		 	   	 .setHeader(KafkaHeaders.TOPIC, "report_topic")
		 	     .build();
	}
} 
