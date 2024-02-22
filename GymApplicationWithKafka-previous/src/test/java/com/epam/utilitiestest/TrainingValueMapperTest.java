package com.epam.utilitiestest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;

import com.epam.dtos.request.TrainingDetailsDto;
import com.epam.dtos.response.NotificationDto;
import com.epam.dtos.response.TrainingReportDto;
import com.epam.entities.User;
import com.epam.utilities.TrainingValueMapper;

public class TrainingValueMapperTest {
	@InjectMocks
	private TrainingValueMapper trainingValueMapper;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testConvertToTrainingReportDto() {
		TrainingDetailsDto trainingDetails = new TrainingDetailsDto();
		trainingDetails.setTrainerUserName("trainerUsername");
		trainingDetails.setTrainingDate(LocalDate.of(2023, 8, 10));
		trainingDetails.setTrainingDuration(5l);

		User userTrainer = new User();
		userTrainer.setFirstName("TrainerFirstName");
		userTrainer.setLastName("TrainerLastName");
		userTrainer.setActive(true);
		userTrainer.setEmail("trainer@example.com");

		TrainingReportDto trainingReportDto = trainingValueMapper.convertToTrainingReportDto(trainingDetails,
				userTrainer);

		assertNotNull(trainingReportDto);
		assertEquals(trainingDetails.getTrainerUserName(), trainingReportDto.getTrainerUserName());
		assertEquals(userTrainer.getFirstName(), trainingReportDto.getTrainerFirstName());
		assertEquals(userTrainer.getLastName(), trainingReportDto.getTrainerLastName());
		assertTrue(trainingReportDto.isTrainerActive());
		assertEquals(userTrainer.getEmail(), trainingReportDto.getTrainerEmail());
		assertEquals(trainingDetails.getTrainingDate(), trainingReportDto.getDate());
		assertEquals(trainingDetails.getTrainingDuration(), trainingReportDto.getDuration());
	}

	@Test
	void testConvertToNotificationDto() {
		TrainingDetailsDto trainingDetails = new TrainingDetailsDto();
		trainingDetails.setTraineeUserName("traineeUsername");
		trainingDetails.setTrainerUserName("trainerUsername");
		trainingDetails.setTrainingName("TrainingName");
		trainingDetails.setTrainingDate(LocalDate.of(2023, 8, 10));
		trainingDetails.setTrainingDuration(5l);

		User userTrainee = new User();
		userTrainee.setEmail("trainee@example.com");

		User userTrainer = new User();
		userTrainer.setEmail("trainer@example.com");

		NotificationDto notificationDto = trainingValueMapper.convertToNotificationDto(trainingDetails, userTrainee,
				userTrainer);

		assertNotNull(notificationDto);
		assertEquals("Training is added Successfully", notificationDto.getSubject());
		assertEquals(2, notificationDto.getToEmails().size());
		assertTrue(notificationDto.getToEmails().contains("trainee@example.com"));
		assertTrue(notificationDto.getToEmails().contains("trainer@example.com"));
		assertEquals(0, notificationDto.getCcEmails().size());
		assertTrue(notificationDto.getBody().contains("Trainee Name :traineeUsername"));
		assertTrue(notificationDto.getBody().contains("Trainer Name :trainerUsername"));
		assertTrue(notificationDto.getBody().contains("Training Name :TrainingName"));
		// ... additional assertions for the rest of the fields
	}

	@Test
	public void testConvertToMessage() {
		NotificationDto notificationDto = NotificationDto.builder().subject("Test Subject")
				.toEmails(List.of("to@example.com")).ccEmails(List.of("cc@example.com")).body("Test Body").build();

		Message<NotificationDto> message = trainingValueMapper.convertToNotificationMessage(notificationDto);

		assertEquals(notificationDto, message.getPayload());
		assertEquals("notification_topic", message.getHeaders().get(KafkaHeaders.TOPIC));
	}

	@Test
	void testConvertToReportMessage() {
		TrainingReportDto trainingReportDto = new TrainingReportDto(/* Set properties here */);

		Message<TrainingReportDto> message = trainingValueMapper.convertToReportMessage(trainingReportDto);

		assertNotNull(message);
		assertEquals(trainingReportDto, message.getPayload());
		assertEquals("report_topic", message.getHeaders().get(KafkaHeaders.TOPIC));
	}
}
