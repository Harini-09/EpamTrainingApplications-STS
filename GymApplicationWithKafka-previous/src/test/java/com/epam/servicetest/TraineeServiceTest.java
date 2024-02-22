package com.epam.servicetest;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;

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
import com.epam.service.TraineeServiceImpl;
import com.epam.utilities.PasswordGenerator;
import com.epam.utilities.TraineeValueMapper;

class TraineeServiceTest {
	@InjectMocks
	private TraineeServiceImpl traineeService;

	@Mock
	private TraineeRepository traineeRepository;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordGenerator passwordGenerator;

	@Mock
	private TraineeValueMapper mapper;

	@Mock
	private KafkaTemplate<String, NotificationDto> kafkaTemplate;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testSaveTrainee_Success() {
		TraineeDetailsDto traineeDetails = new TraineeDetailsDto();
		when(passwordGenerator.generatePassword()).thenReturn("randomPassword");
		User newUser = new User();
		when(mapper.convertToUser(traineeDetails, "randomPassword")).thenReturn(newUser);
		when(userRepository.save(newUser)).thenReturn(newUser);
		Trainee trainee = new Trainee();
		when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);
		NotificationDto notificationDto = new NotificationDto();
		when(mapper.convertToNotificationDtoAfterSavingTrainee(newUser, "randomPassword", traineeDetails, trainee))
				.thenReturn(notificationDto);
		when(kafkaTemplate.send(any(Message.class))).thenReturn(null);
		TraineeBasicDetailsDto result = traineeService.saveTrainee(traineeDetails);

	}  

	@Test
	public void testGetTrainee_Success() throws UserException {
		User user = new User();
		user.setUserName("testUsername");
		Trainee trainee = new Trainee();
		trainee.setUser(user);
		when(userRepository.findByUserName("testUsername")).thenReturn(Optional.of(user));
		when(mapper.convertToTraineeProfileDto(user, trainee)).thenReturn(new TraineeProfileDto());
		when(mapper.convertToTrainerDtoList(trainee)).thenReturn(new ArrayList<>());
		TraineeProfileDto result = traineeService.getTrainee("testUsername");
		assertNotNull(result);
		verify(userRepository, times(1)).findByUserName("testUsername");
		verify(mapper, times(1)).convertToTraineeProfileDto(user, trainee);
		verify(mapper, times(1)).convertToTrainerDtoList(trainee);
	}

	@Test
    public void testGetTrainee_UserNotFound() throws UserException {
        when(userRepository.findByUserName("nonExistentUser")).thenReturn(Optional.empty());
        assertThrows(UserException.class,()->traineeService.getTrainee("nonExistentUser"));
    }

	@Test
	void testUpdateTrainee_Success() throws UserException {
		TraineeProfileUpdate traineeProfileUpdate = new TraineeProfileUpdate();

		User user = new User();
		user.setUserName("adgas");
		Trainee trainee = new Trainee();
		trainee.setUser(user);

		when(userRepository.findByUserName(traineeProfileUpdate.getUsername())).thenReturn(Optional.of(user));

		List<TrainerDto> trainerDtoList = new ArrayList<>();
		when(mapper.convertToTrainerDtoList(user)).thenReturn(trainerDtoList);

		NotificationDto notificationDto = new NotificationDto();
		when(mapper.convertToNotificationDtoAfterUpdatingTrainee(user, traineeProfileUpdate))
				.thenReturn(notificationDto);

		when(kafkaTemplate.send(any(Message.class))).thenReturn(null);

		TraineeProfileDto result = traineeService.updateTrainee(traineeProfileUpdate);

	}

	@Test
	void testUpdateTrainee_UserNotFound() {
		TraineeProfileUpdate traineeProfileUpdate = new TraineeProfileUpdate();

		when(userRepository.findByUserName(traineeProfileUpdate.getUsername())).thenReturn(Optional.empty());

		assertThrows(UserException.class, () -> traineeService.updateTrainee(traineeProfileUpdate));

	}

	@Test
	public void testDeleteTrainee_Success() throws UserException {
		User user = new User();
		user.setUserName("testUsername");

		when(userRepository.findByUserName("testUsername")).thenReturn(Optional.of(user));

		traineeService.deleteTrainee("testUsername");

		verify(userRepository, times(1)).findByUserName("testUsername");

		verify(userRepository, times(1)).delete(user);
	}

	@Test
    public void testDeleteTrainee_UserNotFound() throws UserException {
        when(userRepository.findByUserName("nonExistentUser")).thenReturn(Optional.empty());

        assertThrows(UserException.class,()->traineeService.deleteTrainee("nonExistentUser"));

    }

	@Test
	public void testUpdateTraineeTrainerList_Success() throws UserException {
		User traineeUser = new User();
		traineeUser.setUserName("traineeUsername");
		Trainee trainee = new Trainee();
		traineeUser.setTrainee(trainee);

		User trainerUser1 = new User();
		trainerUser1.setUserName("trainerUsername1");
		Trainer trainer1 = new Trainer();
		trainerUser1.setTrainer(trainer1);

		User trainerUser2 = new User();
		trainerUser2.setUserName("trainerUsername2");
		Trainer trainer2 = new Trainer();
		trainerUser2.setTrainer(trainer2);

		when(userRepository.findByUserName("traineeUsername")).thenReturn(Optional.of(traineeUser));
		when(userRepository.findByUserName("trainerUsername1")).thenReturn(Optional.of(trainerUser1));
		when(userRepository.findByUserName("trainerUsername2")).thenReturn(Optional.of(trainerUser2));

		when(mapper.convertToTrainerDtoList(anyList())).thenReturn(new ArrayList<>());

		List<TrainerDto> result = traineeService.updateTraineeTrainerList("traineeUsername",
				Arrays.asList("trainerUsername1", "trainerUsername2"));

		assertNotNull(result);

		verify(userRepository, times(1)).findByUserName("traineeUsername");
		verify(userRepository, times(1)).findByUserName("trainerUsername1");
		verify(userRepository, times(1)).findByUserName("trainerUsername2");

		verify(mapper, times(1)).convertToTrainerDtoList(anyList());

		verify(userRepository, times(1)).save(traineeUser);
	}

	@Test
    public void testUpdateTraineeTrainerList_TrainerNotFound() throws UserException {
        

        when(userRepository.findByUserName("trainerUsername1")).thenReturn(Optional.empty());        
        
        assertThrows(UserException.class,()->traineeService.updateTraineeTrainerList("traineeUsername", Arrays.asList("trainerUsername1")));

    }

	@Test
	public void testUpdateTraineeTrainerList_TrainerNotFound2() throws UserException {
		User traineeUser = new User();
		traineeUser.setUserName("traineeUsername");
		Trainee trainee = new Trainee();
		traineeUser.setTrainee(trainee);

		when(userRepository.findByUserName("traineeUsername")).thenReturn(Optional.of(traineeUser));
		when(userRepository.findByUserName("trainerUsername2")).thenReturn(Optional.empty());

		assertThrows(UserException.class,
				() -> traineeService.updateTraineeTrainerList("traineeUsername", Arrays.asList("trainerUsername2")));

	}

	@Test
	public void testGetTraineeTrainingsList_Success() throws UserException {
		User user = new User();
		user.setUserName("testUsername");

		Training training1 = new Training();
		Training training2 = new Training();

		List<Training> trainingsList = Arrays.asList(training1, training2);

		when(userRepository.findByUserName("testUsername")).thenReturn(Optional.of(user));

		when(traineeRepository.findTrainingsForTrainee("testUsername", LocalDate.now(), LocalDate.now(), null, null))
				.thenReturn(trainingsList);

		when(mapper.convertToTraineeTrainingResponseDto(trainingsList)).thenReturn(new ArrayList<>());

		List<TraineeTrainingResponseDto> result = traineeService.getTraineeTrainingsList("testUsername",
				LocalDate.now(), LocalDate.now(), null, null);

		assertNotNull(result);

		verify(userRepository, times(1)).findByUserName("testUsername");

		verify(traineeRepository, times(1)).findTrainingsForTrainee("testUsername", LocalDate.now(), LocalDate.now(),
				null, null);

		verify(mapper, times(1)).convertToTraineeTrainingResponseDto(trainingsList);
	}

	@Test
    public void testGetTraineeTrainingsList_UserNotFound() throws UserException {
        when(userRepository.findByUserName("nonExistentUser")).thenReturn(Optional.empty());
       assertThrows(UserException.class,()->traineeService.getTraineeTrainingsList("nonExistentUser", LocalDate.now(), LocalDate.now(), null, null));
    }

}
