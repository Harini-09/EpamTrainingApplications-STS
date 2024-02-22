package com.epam.utilitiestest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.epam.customexceptions.TrainerException;
import com.epam.dtos.request.TrainerDetailsDto;
import com.epam.dtos.response.NotificationDto;
import com.epam.dtos.response.TraineeDto;
import com.epam.dtos.response.TrainerBasicDetailsDto;
import com.epam.dtos.response.TrainerDto;
import com.epam.dtos.response.TrainerProfileDto;
import com.epam.dtos.response.TrainerTrainingResponseDto;
import com.epam.entities.Trainee;
import com.epam.entities.Trainer;
import com.epam.entities.Training;
import com.epam.entities.TrainingType;
import com.epam.entities.User;
import com.epam.utilities.TrainerValueMapper;
import com.epam.utilities.UserNameGenerator;

public class TrainerValueMapperTest {
	@InjectMocks
	private TrainerValueMapper trainerValueMapper;

	@Mock
	private UserNameGenerator userNameGenerator;

	@Mock
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testConvertToUser() {
		TrainerDetailsDto trainerDetails = new TrainerDetailsDto();
		trainerDetails.setFirstName("John");
		trainerDetails.setLastName("Doe");
		trainerDetails.setEmail("john@example.com");

		when(userNameGenerator.generateUserName("john@example.com")).thenReturn("john_doe");
		when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");

		User user = trainerValueMapper.convertToUser(trainerDetails, "testPassword");

		assertEquals("John", user.getFirstName());
		assertEquals("Doe", user.getLastName());
		assertEquals("john@example.com", user.getEmail());
		assertEquals("encodedPassword", user.getPassword());
	}

	@Test
	public void testConvertToNotificationDtoAfterSavingTrainer() {
		User newUser = new User();
		newUser.setUserName("john_doe");
		newUser.setPassword("encodedPassword");
		newUser.setFirstName("John");
		newUser.setLastName("Doe");
		newUser.setEmail("john@example.com");
		newUser.setActive(true);

		// Create a Trainer object
		Trainer trainer = new Trainer();
		TrainingType t = new TrainingType();
		t.setTrainingTypeName("adsga");
		trainer.setUser(newUser); // Create a TrainerDetailsDto object
		TrainerDetailsDto trainerDetails = new TrainerDetailsDto();
		trainerDetails.setTrainingTypeName("Some Training Type");

		// Mock the behavior of userNameGenerator
		when(userNameGenerator.generateUserName("john@example.com")).thenReturn("john_doe");

		// Call the method being tested
		NotificationDto notificationDto = trainerValueMapper.convertToNotificationDtoAfterSavingTrainer(newUser,
				trainer, trainerDetails);

		// Assert the results
		assertEquals("Trainer is registered successfully!!", notificationDto.getSubject());
		assertEquals(List.of("john@example.com"), notificationDto.getToEmails());
		assertEquals(List.of(), notificationDto.getCcEmails());
	}

	@Test
	public void testConvertToTrainerBasicDetailsDto() {
		// Create a User object
		User newUser = new User();
		newUser.setUserName("john_doe");
		newUser.setPassword("encodedPassword");
		String randomPassword = "hello";
		// Call the method being tested
		TrainerBasicDetailsDto basicDetailsDto = trainerValueMapper.convertToTrainerBasicDetailsDto(newUser,randomPassword);

		// Assert the results
		assertEquals("john_doe", basicDetailsDto.getUserName());
		assertEquals("hello", randomPassword);
	}

	@Test
	public void testConvertToTraineeDtoList() {
		// Create a Trainer object
		Trainer trainer = new Trainer();

		// Create a Trainee
		Trainee trainee = new Trainee();
		User user = new User();
		user.setUserName("trainee_user");
		user.setFirstName("Trainee");
		user.setLastName("User");
		trainee.setUser(user);

		// Add the trainee to the trainer's traineesList
		trainer.getTraineesList().add(trainee);

		// Call the method being tested
		List<TraineeDto> traineeDtoList = trainerValueMapper.convertToTraineeDtoList(trainer);

		// Assert the results
		assertEquals(1, traineeDtoList.size());
		TraineeDto traineeDto = traineeDtoList.get(0);
		assertEquals("trainee_user", traineeDto.getUsername());
		assertEquals("Trainee", traineeDto.getFirstName());
		assertEquals("User", traineeDto.getLastName());
	}

	@Test
	public void testConvertToTrainerProfileDto() {
		// Create a User object
		User user = new User();
		user.setFirstName("Trainer");
		user.setLastName("User");
		user.setActive(true);
		TrainingType t = new TrainingType();
		t.setTrainingTypeName("zumba");
		// Create a Trainer object
		Trainer trainer = new Trainer();
		trainer.setTrainingType(t);

		// Create a TraineeDto list
		List<TraineeDto> traineeDtoList = new ArrayList<>();
		traineeDtoList.add(TraineeDto.builder().username("trainee_user").firstName("Trainee").lastName("User").build());

		// Call the method being tested
		TrainerProfileDto profileDto = trainerValueMapper.convertToTrainerProfileDto(user, trainer, traineeDtoList);

		// Assert the results
		assertEquals("Trainer", profileDto.getFirstName());
		assertEquals("User", profileDto.getLastName());

	}

	@Test
	void testConvertToTraineeDtoListForUpdate() {
		Trainer trainer = new Trainer();

		Trainee trainee1 = new Trainee();
		User user1 = new User();
		user1.setUserName("trainee1");
		user1.setFirstName("Trainee");
		user1.setLastName("One");
		trainee1.setUser(user1);

		Trainee trainee2 = new Trainee();
		User user2 = new User();
		user2.setUserName("trainee2");
		user2.setFirstName("Trainee");
		user2.setLastName("Two");
		trainee2.setUser(user2);

		trainer.getTraineesList().add(trainee1);
		trainer.getTraineesList().add(trainee2);

		List<TraineeDto> traineeDtoList = trainerValueMapper.convertToTraineeDtoListForUpdate(trainer);

		assertNotNull(traineeDtoList);
		assertEquals(2, traineeDtoList.size());

		TraineeDto traineeDto1 = traineeDtoList.get(0);
		assertEquals("trainee1", traineeDto1.getUsername());
		assertEquals("Trainee", traineeDto1.getFirstName());
		assertEquals("One", traineeDto1.getLastName());

		TraineeDto traineeDto2 = traineeDtoList.get(1);
		assertEquals("trainee2", traineeDto2.getUsername());
		assertEquals("Trainee", traineeDto2.getFirstName());
		assertEquals("Two", traineeDto2.getLastName());

	}

	@Test
	void testConvertToNotificationDtoAfterUpdatingTrainer() {
		User newUser = new User();
		newUser.setUserName("john_doe");
		newUser.setPassword("encodedPassword");
		newUser.setFirstName("John");
		newUser.setLastName("Doe");
		newUser.setEmail("john@example.com");
		newUser.setActive(true);

		// Create a Trainer object
		Trainer trainer = new Trainer();
		TrainingType t = new TrainingType();
		t.setTrainingTypeName("adsga");
		trainer.setUser(newUser);
		NotificationDto notificationDto = trainerValueMapper.convertToNotificationDtoAfterUpdatingTrainer(newUser);

		assertNotNull(notificationDto);

	}

	@Test
	void testConvertToTrainerProfileDtoForUpdate() {
		User user = new User();
		user.setFirstName("Trainer");
		user.setLastName("One");
		user.setActive(true);

		Trainer trainer = new Trainer();
		TrainingType trainingType = new TrainingType();
		trainingType.setTrainingTypeName("Technical");
		trainer.setTrainingType(trainingType);

		TraineeDto traineeDto1 = TraineeDto.builder().username("trainee1").firstName("Trainee").lastName("One").build();

		TraineeDto traineeDto2 = TraineeDto.builder().username("trainee2").firstName("Trainee").lastName("Two").build();

		List<TraineeDto> traineeDtoList = List.of(traineeDto1, traineeDto2);

		TrainerProfileDto trainerProfileDto = trainerValueMapper.convertToTrainerProfileDtoForUpdate(user, trainer,
				traineeDtoList);

		assertNotNull(trainerProfileDto);
	}

	@Test
	void testConvertToTrainerDto() throws TrainerException {
		User user = new User();
		user.setTrainee(new Trainee()); // Setting trainee to the user

		User trainerUser = new User();
		trainerUser.setUserName("trainer1");
		trainerUser.setFirstName("Trainer");
		trainerUser.setLastName("One");
		trainerUser.setActive(true);

		Trainer trainer1 = new Trainer();
		TrainingType trainingType1 = new TrainingType();
		trainingType1.setTrainingTypeName("Technical");
		trainer1.setTrainingType(trainingType1);
		trainer1.setUser(trainerUser);

		Trainer trainer2 = new Trainer();
		TrainingType trainingType2 = new TrainingType();
		trainingType2.setTrainingTypeName("Soft Skills");
		trainer2.setTrainingType(trainingType2);
		trainer2.setUser(new User()); // Inactive trainer

		List<Trainer> trainersList = new ArrayList<>();
		trainersList.add(trainer1);
		trainersList.add(trainer2);

		TrainerDto trainerDto = trainerValueMapper.convertToTrainerDto(trainersList, user);

		assertNotNull(trainerDto);
	}

	@Test
	void testConvertToTrainerDtoNoTrainersAvailable() {
		User user = new User();
		user.setTrainee(new Trainee()); // Setting trainee to the user

		List<Trainer> emptyTrainersList = new ArrayList<>();

		assertThrows(TrainerException.class, () -> trainerValueMapper.convertToTrainerDto(emptyTrainersList, user));
	}

	@Test
	void testConvertToTrainerTrainingsList() {
		TrainingType trainingType = new TrainingType();
		trainingType.setTrainingTypeName("Technical");

		Trainee trainee = new Trainee();
		User traineeUser = new User();
		traineeUser.setUserName("trainee1");
		trainee.setUser(traineeUser);

		Training training1 = new Training();
		training1.setTrainingName("Java Basics");
		training1.setTrainingDate(LocalDate.of(2023, 8, 15));
		training1.setTrainingType(trainingType);
		training1.setTrainingDuration(3L);
		training1.setTrainee(trainee);

		Training training2 = new Training();
		training2.setTrainingName("Database Concepts");
		training2.setTrainingDate(LocalDate.of(2023, 8, 20));
		training2.setTrainingType(trainingType);
		training2.setTrainingDuration(2L);
		training2.setTrainee(trainee);

		List<Training> trainerTrainings = new ArrayList<>();
		trainerTrainings.add(training1);
		trainerTrainings.add(training2);

		List<TrainerTrainingResponseDto> trainerTrainingResponseDtos = trainerValueMapper
				.convertToTrainerTrainingsList(trainerTrainings);
 
		assertNotNull(trainerTrainingResponseDtos);
	}

	@Test
	public void testConvertToMessage() {
		NotificationDto notificationDto = NotificationDto.builder().subject("Test Subject")
				.toEmails(List.of("to@example.com")).ccEmails(List.of("cc@example.com")).body("Test Body").build();

		Message<NotificationDto> message = trainerValueMapper.convertToMessage(notificationDto);

		assertEquals(notificationDto, message.getPayload());
		assertEquals("notification_topic", message.getHeaders().get(KafkaHeaders.TOPIC));
	}

}
