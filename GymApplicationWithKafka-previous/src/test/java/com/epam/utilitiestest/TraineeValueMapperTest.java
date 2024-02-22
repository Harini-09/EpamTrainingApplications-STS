package com.epam.utilitiestest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.security.crypto.password.PasswordEncoder;

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
import com.epam.entities.TrainingType;
import com.epam.entities.User;
import com.epam.utilities.TraineeValueMapper;
import com.epam.utilities.UserNameGenerator;

public class TraineeValueMapperTest {
	@InjectMocks
	private TraineeValueMapper traineeValueMapper;

	@Mock
	private UserNameGenerator userNameGenerator;

	@Mock
	private PasswordEncoder passwordEncoder;

	@BeforeEach
	public void setUp() {
	    MockitoAnnotations.openMocks(this);
	} 
  
	@Test
	public void testConvertToUser() {
	    String randomPassword = "randomPassword";

	    TraineeDetailsDto traineeDetails = new TraineeDetailsDto();
	    traineeDetails.setFirstName("John");
	    traineeDetails.setLastName("Doe");
	    traineeDetails.setEmail("john.doe@example.com");
	    traineeDetails.setAddress("123 Main St");

	    User convertedUser = traineeValueMapper.convertToUser(traineeDetails, randomPassword);

	    assertNotNull(convertedUser);
	    assertEquals("John", convertedUser.getFirstName());
	    assertEquals("Doe", convertedUser.getLastName());
	    assertEquals("john.doe@example.com", convertedUser.getEmail());
	    assertTrue(convertedUser.isActive());
	   
	}

	@Test  
	public void testConvertToNotificationDtoAfterSavingTrainee() {
	    User newUser = new User();
	    newUser.setUserName("john_doe");
	    newUser.setFirstName("John");
	    newUser.setLastName("Doe");
	    newUser.setEmail("john.doe@example.com");
	    newUser.setActive(true);

	    TraineeDetailsDto traineeDetails = new TraineeDetailsDto();
	    traineeDetails.setAddress("123 Main St");

	    Trainee trainee = new Trainee();
	    trainee.setUser(newUser);

	    String randomPassword = "randomPassword";

	    // Call the method
	    NotificationDto notificationDto = traineeValueMapper.convertToNotificationDtoAfterSavingTrainee(newUser, randomPassword, traineeDetails, trainee);

	    assertNotNull(notificationDto);
	    assertEquals("Trainee is registered successfully!!", notificationDto.getSubject());
	    assertNotNull(notificationDto.getToEmails());
	    assertTrue(notificationDto.getToEmails().contains("john.doe@example.com"));
	    assertTrue(notificationDto.getBody().contains("First Name : John"));
	    // You can further test other attributes and interactions if needed
	} 
	
	@Test
	public void testConvertToTraineeBasicDetailsDto() {
	    User newUser = new User();
	    newUser.setUserName("john_doe");
	    
	    String randomPassword = "randomPassword";
	    
	    TraineeBasicDetailsDto basicDetailsDto = traineeValueMapper.convertToTraineeBasicDetailsDto(newUser, randomPassword);

	    assertEquals("john_doe", basicDetailsDto.getUserName());
	    assertEquals("randomPassword", basicDetailsDto.getPassword());
	}

	@Test
	public void testConvertToTraineeProfileDto() {
	    User user = new User();
	    user.setFirstName("John");
	    user.setLastName("Doe");
	    user.setActive(true);
 
	    Trainee trainee = new Trainee();
	    trainee.setDateOfBirth(LocalDate.of(1990, 5, 15));
	    trainee.setAddress("123 Main St");

	    TraineeProfileDto profileDto = traineeValueMapper.convertToTraineeProfileDto(user, trainee);

	    assertEquals("John", profileDto.getFirstName());
	    assertEquals("Doe", profileDto.getLastName());
	    assertEquals(LocalDate.of(1990, 5, 15), profileDto.getDateOfBirth());
	    assertEquals("123 Main St", profileDto.getAddress());
	    assertTrue(profileDto.isActive());
	}
	
	@Test
	public void testConvertToTrainerDtoListFromTrainee() {
		Trainee trainee = new Trainee();
		User user1 = new User();
		user1.setUserName("trainee1");  
		trainee.setUser(user1);
	    trainee.setAddress("123 Main St");
	    
	    Trainer trainer1 = new Trainer();
	    User user2 = new User();
		user2.setUserName("trainer1");  
	    trainer1.setUser(user2);
	    TrainingType t = new TrainingType();
	    t.setTrainingTypeName("Zumba");	    
	    trainer1.setTrainingType(t);

	    Trainer trainer2 = new Trainer();
	    User user3 = new User();
		user2.setUserName("trainer2");  
	    trainer2.setUser(user3);
	    TrainingType t2 = new TrainingType();
	    t.setTrainingTypeName("Fitness");	 
	    trainer2.setTrainingType(t2);
	    
	    trainee.setTrainersList(List.of(trainer1, trainer2));

	    List<TrainerDto> trainerDtoList = traineeValueMapper.convertToTrainerDtoList(trainee);

	    assertEquals(2, trainerDtoList.size());

	}

	@Test 
	public void testConvertToTrainerDtoListFromUser() {
		Trainee trainee = new Trainee();
		User user1 = new User();
		user1.setUserName("trainee1");  
		trainee.setUser(user1);
	    trainee.setAddress("123 Main St");
	    
	    Trainer trainer1 = new Trainer();
	    User user2 = new User();
		user2.setUserName("trainer1");  
	    trainer1.setUser(user2);
	    TrainingType t = new TrainingType();
	    t.setTrainingTypeName("Zumba");	    
	    trainer1.setTrainingType(t);

	    Trainer trainer2 = new Trainer();
	    User user3 = new User();
		user2.setUserName("trainer2");  
	    trainer2.setUser(user3);
	    TrainingType t2 = new TrainingType();
	    t.setTrainingTypeName("Fitness");	 
	    trainer2.setTrainingType(t2);
	    List<Trainer> ts = List.of(trainer1, trainer2);
	    trainee.setTrainersList(List.of(trainer1, trainer2));

	    List<TrainerDto> trainerDtoList = traineeValueMapper.convertToTrainerDtoList(user1);

	    assertEquals(2, trainerDtoList.size());

	    
	}
	
	@Test
	public void testConvertToNotificationDtoAfterUpdatingTrainee() {
	    User user = new User(0, "trainee1", "Trainee", "User", null, false, null, null, null);
	    user.setEmail("trainee@example.com");

	    TraineeProfileUpdate traineeProfile = new TraineeProfileUpdate();
	    traineeProfile.setFirstName("Updated");
	    traineeProfile.setLastName("Trainee");
	    traineeProfile.setAddress("123 Main St");
	    traineeProfile.setActive(true);

	    NotificationDto notificationDto = traineeValueMapper.convertToNotificationDtoAfterUpdatingTrainee(user, traineeProfile);

	    assertEquals("Trainee profile is updated successfully!!", notificationDto.getSubject());
	    assertEquals(List.of("trainee@example.com"), notificationDto.getToEmails());
	    assertEquals(List.of(), notificationDto.getCcEmails());
	   
	}

	@Test
	public void testConvertToTraineeProfileDto2() {
	    TraineeProfileUpdate traineeProfile = new TraineeProfileUpdate();
	    traineeProfile.setFirstName("John");
	    traineeProfile.setLastName("Doe");
	    traineeProfile.setDateOfBirth(LocalDate.of(1990, 1, 1));
	    traineeProfile.setAddress("456 Second St");
	    traineeProfile.setActive(true);

	    List<TrainerDto> trainerDtoList = List.of(
	            new TrainerDto("trainer1", "Trainer", "One", "Fitness"),
	            new TrainerDto("trainer2", "Trainer", "Two", "Yoga")
	    );

	    TraineeProfileDto traineeProfileDto = traineeValueMapper.convertToTraineeProfileDto(traineeProfile, trainerDtoList);

	    assertEquals("John", traineeProfileDto.getFirstName());
	    assertEquals("Doe", traineeProfileDto.getLastName());
	    assertEquals(LocalDate.of(1990, 1, 1), traineeProfileDto.getDateOfBirth());
	    assertEquals("456 Second St", traineeProfileDto.getAddress());
	    assertTrue(traineeProfileDto.isActive());
	    assertEquals(2, traineeProfileDto.getTrainersList().size());
	    // Similar assertions for other attributes of trainersList

	}
	
	@Test
	public void testConvertToTraineeTrainingResponseDto() {
	    Training training1 = new Training();
	    training1.setTrainingName("Fitness Training");
	    training1.setTrainingDate(LocalDate.of(2023, 8, 15));
	    training1.setTrainingDuration(60l);
	    TrainingType trainingType1 = new TrainingType();
	    trainingType1.setTrainingTypeName("fitness");
	    training1.setTrainingType(trainingType1);
	    Trainer trainer1 = new Trainer();
	    trainer1.setUser(new User(0, "trainer1", "Trainer", "One", null, false, null, null, trainer1));
	    training1.setTrainer(trainer1); 
 
	    Training training2 = new Training();
	    training2.setTrainingName("Yoga Training");
	    training2.setTrainingDate(LocalDate.of(2023, 8, 16));
	    training2.setTrainingDuration(45l);
	    TrainingType trainingType2 = new TrainingType();
	    trainingType2.setTrainingTypeName("Zumba");
	    training2.setTrainingType(trainingType2);
	    Trainer trainer2 = new Trainer();
	    trainer2.setUser(new User(0, "trainer2", "Trainer", "Two", null, false, null, null, trainer2));
	    training2.setTrainer(trainer2);

	    List<Training> traineeTrainings = List.of(training1, training2);

	    List<TraineeTrainingResponseDto> trainingResponseDtoList = traineeValueMapper.convertToTraineeTrainingResponseDto(traineeTrainings);

	    assertEquals(2, trainingResponseDtoList.size());

	   
	}
 
	@Test
	public void testConvertToMessage() {
	    NotificationDto notificationDto = NotificationDto.builder()
	            .subject("Test Subject")
	            .toEmails(List.of("to@example.com"))
	            .ccEmails(List.of("cc@example.com"))
	            .body("Test Body")
	            .build();

	    Message<NotificationDto> message = traineeValueMapper.convertToMessage(notificationDto);

	    assertEquals(notificationDto, message.getPayload());
	    assertEquals("notification_topic", message.getHeaders().get(KafkaHeaders.TOPIC));
	}
	
	@Test 
	public void testConvertToTrainerDtoList() {
		Trainee trainee = new Trainee();
		User user1 = new User();
		user1.setUserName("trainee1");  
		trainee.setUser(user1);
	    trainee.setAddress("123 Main St");
	    
	    Trainer trainer1 = new Trainer();
	    User user2 = new User();
		user2.setUserName("trainer1");  
	    trainer1.setUser(user2);
	    TrainingType t = new TrainingType();
	    t.setTrainingTypeName("Zumba");	    
	    trainer1.setTrainingType(t);

	    Trainer trainer2 = new Trainer();
	    User user3 = new User();
		user2.setUserName("trainer2");  
	    trainer2.setUser(user3);
	    TrainingType t2 = new TrainingType();
	    t.setTrainingTypeName("Fitness");	 
	    trainer2.setTrainingType(t2);
	    List<Trainer> ts = List.of(trainer1, trainer2);
	    trainee.setTrainersList(List.of(trainer1, trainer2));

	    List<TrainerDto> trainerDtoList = traineeValueMapper.convertToTrainerDtoList(ts);

	    assertEquals(2, trainerDtoList.size());

	    
	}


}
